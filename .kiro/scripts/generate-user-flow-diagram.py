#!/usr/bin/env python3
"""
Kiro User Flow Diagram Generator

Script tự động tạo user flow diagram từ requirements.md và design.md
Giúp visualize luồng người dùng và kết nối giữa các màn hình

Usage:
    python generate-user-flow-diagram.py <project-name>
    python generate-user-flow-diagram.py blood-pressure-tracking
"""

import os
import sys
import re
import json
from pathlib import Path
from typing import Dict, List, Tuple, Optional

class UserFlowDiagramGenerator:
    def __init__(self, project_name: str):
        self.project_name = project_name
        self.project_path = Path(f".kiro/specs/{project_name}")
        self.requirements_file = self.project_path / "requirements.md"
        self.design_file = self.project_path / "design.md"
        self.user_flows_file = self.project_path / "user-flows.md"
        
        # Extracted data
        self.screens = []
        self.user_stories = []
        self.navigation_flows = []
        self.data_flows = []
        
    def validate_project_structure(self) -> bool:
        """Kiểm tra cấu trúc project có hợp lệ không"""
        if not self.project_path.exists():
            print(f"❌ Project directory không tồn tại: {self.project_path}")
            return False
            
        if not self.requirements_file.exists():
            print(f"❌ Requirements file không tồn tại: {self.requirements_file}")
            return False
            
        if not self.design_file.exists():
            print(f"❌ Design file không tồn tại: {self.design_file}")
            return False
            
        return True
        
    def extract_screens_from_requirements(self) -> List[str]:
        """Trích xuất danh sách màn hình từ requirements.md"""
        screens = set()
        
        with open(self.requirements_file, 'r', encoding='utf-8') as f:
            content = f.read()
            
        # Tìm các từ khóa màn hình
        screen_patterns = [
            r'màn hình ([\w\s]+)',
            r'screen ([\w\s]+)',
            r'page ([\w\s]+)',
            r'view ([\w\s]+)',
            r'UI ([\w\s]+)',
            r'interface ([\w\s]+)'
        ]
        
        for pattern in screen_patterns:
            matches = re.findall(pattern, content, re.IGNORECASE)
            for match in matches:
                screen_name = match.strip().title()
                if len(screen_name) > 2:  # Lọc các từ quá ngắn
                    screens.add(screen_name)
                    
        # Tìm trong user stories
        user_story_pattern = r'As a.*?I want.*?So that.*?'
        user_stories = re.findall(user_story_pattern, content, re.DOTALL | re.IGNORECASE)
        
        for story in user_stories:
            # Tìm màn hình được đề cập trong user story
            for pattern in screen_patterns:
                matches = re.findall(pattern, story, re.IGNORECASE)
                for match in matches:
                    screen_name = match.strip().title()
                    if len(screen_name) > 2:
                        screens.add(screen_name)
                        
        self.screens = list(screens)
        return self.screens
        
    def extract_navigation_from_design(self) -> List[Dict]:
        """Trích xuất navigation flow từ design.md"""
        navigation_flows = []
        
        with open(self.design_file, 'r', encoding='utf-8') as f:
            content = f.read()
            
        # Tìm các mô tả navigation
        navigation_patterns = [
            r'([\w\s]+)\s*→\s*([\w\s]+)',
            r'([\w\s]+)\s*->\s*([\w\s]+)',
            r'from ([\w\s]+) to ([\w\s]+)',
            r'navigate from ([\w\s]+) to ([\w\s]+)',
            r'chuyển từ ([\w\s]+) sang ([\w\s]+)'
        ]
        
        for pattern in navigation_patterns:
            matches = re.findall(pattern, content, re.IGNORECASE)
            for match in matches:
                if len(match) == 2:
                    from_screen = match[0].strip().title()
                    to_screen = match[1].strip().title()
                    
                    if len(from_screen) > 2 and len(to_screen) > 2:
                        navigation_flows.append({
                            'from': from_screen,
                            'to': to_screen,
                            'type': 'navigation'
                        })
                        
        self.navigation_flows = navigation_flows
        return navigation_flows
        
    def extract_user_stories(self) -> List[Dict]:
        """Trích xuất user stories từ requirements.md"""
        user_stories = []
        
        with open(self.requirements_file, 'r', encoding='utf-8') as f:
            content = f.read()
            
        # Tìm user stories theo format chuẩn
        story_pattern = r'As a ([^,]+),\s*I want ([^,]+),\s*So that ([^.]+)'
        matches = re.findall(story_pattern, content, re.IGNORECASE | re.DOTALL)
        
        for i, match in enumerate(matches):
            user_stories.append({
                'id': f'US{i+1:03d}',
                'actor': match[0].strip(),
                'action': match[1].strip(),
                'benefit': match[2].strip()
            })
            
        self.user_stories = user_stories
        return user_stories
        
    def generate_mermaid_diagram(self) -> str:
        """Tạo Mermaid diagram cho user flow"""
        mermaid_code = "```mermaid\n"
        mermaid_code += "graph TD\n"
        
        # Thêm các nodes (screens)
        screen_ids = {}
        for i, screen in enumerate(self.screens):
            screen_id = f"S{i+1}"
            screen_ids[screen] = screen_id
            mermaid_code += f"    {screen_id}[{screen}]\n"
            
        # Thêm các connections
        for flow in self.navigation_flows:
            from_id = screen_ids.get(flow['from'])
            to_id = screen_ids.get(flow['to'])
            
            if from_id and to_id:
                mermaid_code += f"    {from_id} --> {to_id}\n"
                
        # Thêm styling
        mermaid_code += "\n    classDef primaryScreen fill:#e1f5fe\n"
        mermaid_code += "    classDef secondaryScreen fill:#f3e5f5\n"
        mermaid_code += "    classDef actionScreen fill:#e8f5e8\n"
        
        mermaid_code += "```\n"
        return mermaid_code
        
    def generate_connection_matrix(self) -> str:
        """Tạo ma trận kết nối giữa các màn hình"""
        matrix = "\n### Screen Connection Matrix\n\n"
        matrix += "| From \\ To | " + " | ".join(self.screens) + " |\n"
        matrix += "|" + "---|" * (len(self.screens) + 1) + "\n"
        
        for from_screen in self.screens:
            row = f"| **{from_screen}** |"
            for to_screen in self.screens:
                # Kiểm tra có connection không
                has_connection = any(
                    flow['from'] == from_screen and flow['to'] == to_screen 
                    for flow in self.navigation_flows
                )
                row += " ✅ |" if has_connection else " ❌ |"
            matrix += row + "\n"
            
        return matrix
        
    def generate_user_flows_content(self) -> str:
        """Tạo nội dung hoàn chỉnh cho user-flows.md"""
        content = f"# {self.project_name.title()} - User Flows & Screen Navigation\n\n"
        
        # Project Overview
        content += "## 1. Project Overview\n\n"
        content += f"**Project**: {self.project_name.replace('-', ' ').title()}\n"
        content += f"**Generated**: {self._get_current_timestamp()}\n"
        content += f"**Total Screens**: {len(self.screens)}\n"
        content += f"**Total User Stories**: {len(self.user_stories)}\n"
        content += f"**Navigation Flows**: {len(self.navigation_flows)}\n\n"
        
        # User Stories Summary
        if self.user_stories:
            content += "## 2. User Stories Summary\n\n"
            for story in self.user_stories:
                content += f"**{story['id']}**: As a {story['actor']}, I want {story['action']}, so that {story['benefit']}.\n\n"
                
        # Screen Navigation Map
        content += "## 3. Screen Navigation Map\n\n"
        content += "### 3.1 Visual Flow Diagram\n\n"
        content += self.generate_mermaid_diagram()
        content += "\n"
        
        # Screen List
        content += "### 3.2 Screen Inventory\n\n"
        for i, screen in enumerate(self.screens, 1):
            content += f"{i}. **{screen}**\n"
        content += "\n"
        
        # Navigation Rules
        content += "### 3.3 Navigation Rules\n\n"
        content += "#### Forward Navigation (Tiến)\n"
        forward_flows = [f for f in self.navigation_flows if 'back' not in f.get('type', '').lower()]
        for flow in forward_flows:
            content += f"- {flow['from']} → {flow['to']}\n"
            
        content += "\n#### Backward Navigation (Lùi)\n"
        content += "- Tất cả màn hình hỗ trợ Back button\n"
        content += "- Navigation stack được quản lý tự động\n\n"
        
        # Connection Matrix
        content += "## 4. Screen Connection Analysis\n\n"
        content += self.generate_connection_matrix()
        
        # Missing Connections
        content += "\n### Missing Connections Analysis\n\n"
        missing_connections = self._find_missing_connections()
        if missing_connections:
            content += "**Potential Missing Connections**:\n"
            for connection in missing_connections:
                content += f"- {connection['from']} → {connection['to']}: {connection['reason']}\n"
        else:
            content += "✅ All expected connections are properly defined.\n"
            
        # Data Flow
        content += "\n## 5. Data Flow Between Screens\n\n"
        content += "### 5.1 Data Passing Rules\n\n"
        content += "| From Screen | To Screen | Data Passed | Method |\n"
        content += "|-------------|-----------|-------------|--------|\n"
        
        for flow in self.navigation_flows:
            content += f"| {flow['from']} | {flow['to']} | {self._infer_data_type(flow)} | Navigation Args |\n"
            
        # Implementation Guidelines
        content += "\n## 6. Implementation Guidelines\n\n"
        content += "### 6.1 Navigation Implementation\n\n"
        content += "```kotlin\n"
        content += "// Example navigation implementation\n"
        content += "navController.navigate(\"destination_route\") {\n"
        content += "    popUpTo(\"source_route\") { inclusive = false }\n"
        content += "    launchSingleTop = true\n"
        content += "}\n"
        content += "```\n\n"
        
        content += "### 6.2 Data Passing Implementation\n\n"
        content += "```kotlin\n"
        content += "// Example data passing\n"
        content += "navController.navigate(\"destination/{param}\".replace(\"{param}\", value))\n"
        content += "```\n\n"
        
        # Validation Checklist
        content += "## 7. Validation Checklist\n\n"
        content += "### Pre-Implementation Validation\n\n"
        content += "- [ ] All screens have clear navigation paths\n"
        content += "- [ ] No orphaned screens (screens without incoming navigation)\n"
        content += "- [ ] No dead-end screens (screens without outgoing navigation)\n"
        content += "- [ ] Data flow is consistent across all navigation paths\n"
        content += "- [ ] Back navigation is properly handled\n"
        content += "- [ ] Deep linking scenarios are considered\n\n"
        
        content += "### Post-Implementation Validation\n\n"
        content += "- [ ] All navigation flows work as expected\n"
        content += "- [ ] Data is properly passed between screens\n"
        content += "- [ ] Navigation stack is managed correctly\n"
        content += "- [ ] No memory leaks in navigation\n"
        content += "- [ ] User can complete all primary user journeys\n"
        
        return content
        
    def _get_current_timestamp(self) -> str:
        """Lấy timestamp hiện tại"""
        from datetime import datetime
        return datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        
    def _find_missing_connections(self) -> List[Dict]:
        """Tìm các kết nối có thể bị thiếu"""
        missing = []
        
        # Logic đơn giản: nếu có màn hình A và B, có thể cần kết nối
        for i, screen_a in enumerate(self.screens):
            for j, screen_b in enumerate(self.screens):
                if i != j:
                    # Kiểm tra có connection từ A đến B không
                    has_connection = any(
                        flow['from'] == screen_a and flow['to'] == screen_b 
                        for flow in self.navigation_flows
                    )
                    
                    if not has_connection:
                        # Đưa ra lý do có thể cần kết nối
                        reason = self._infer_connection_reason(screen_a, screen_b)
                        if reason:
                            missing.append({
                                'from': screen_a,
                                'to': screen_b,
                                'reason': reason
                            })
                            
        return missing[:5]  # Chỉ lấy 5 cái đầu để tránh quá dài
        
    def _infer_connection_reason(self, from_screen: str, to_screen: str) -> Optional[str]:
        """Suy luận lý do có thể cần kết nối giữa 2 màn hình"""
        from_lower = from_screen.lower()
        to_lower = to_screen.lower()
        
        # Các pattern thường gặp
        if 'list' in from_lower and 'detail' in to_lower:
            return "List to detail navigation"
        elif 'main' in from_lower or 'home' in from_lower:
            return "Navigation from main screen"
        elif 'setting' in to_lower:
            return "Access to settings"
        elif 'profile' in to_lower:
            return "Access to user profile"
            
        return None
        
    def _infer_data_type(self, flow: Dict) -> str:
        """Suy luận loại data được truyền giữa các màn hình"""
        from_screen = flow['from'].lower()
        to_screen = flow['to'].lower()
        
        if 'list' in from_screen and 'detail' in to_screen:
            return "Item ID, Item Data"
        elif 'form' in from_screen or 'input' in from_screen:
            return "Form Data"
        elif 'setting' in from_screen:
            return "Configuration Data"
        else:
            return "Navigation State"
            
    def generate(self) -> bool:
        """Thực hiện generate user flow diagram"""
        print(f"🚀 Generating user flow diagram for project: {self.project_name}")
        
        # Validate project structure
        if not self.validate_project_structure():
            return False
            
        print("📖 Extracting information from requirements and design files...")
        
        # Extract data
        self.extract_screens_from_requirements()
        self.extract_navigation_from_design()
        self.extract_user_stories()
        
        print(f"✅ Found {len(self.screens)} screens")
        print(f"✅ Found {len(self.navigation_flows)} navigation flows")
        print(f"✅ Found {len(self.user_stories)} user stories")
        
        # Generate content
        print("📝 Generating user-flows.md content...")
        content = self.generate_user_flows_content()
        
        # Write to file
        with open(self.user_flows_file, 'w', encoding='utf-8') as f:
            f.write(content)
            
        print(f"✅ User flow diagram generated successfully: {self.user_flows_file}")
        print("\n📋 Summary:")
        print(f"   - Screens identified: {len(self.screens)}")
        print(f"   - Navigation flows: {len(self.navigation_flows)}")
        print(f"   - User stories: {len(self.user_stories)}")
        print(f"   - Output file: {self.user_flows_file}")
        
        return True

def main():
    """Main function"""
    if len(sys.argv) != 2:
        print("Usage: python generate-user-flow-diagram.py <project-name>")
        print("Example: python generate-user-flow-diagram.py blood-pressure-tracking")
        sys.exit(1)
        
    project_name = sys.argv[1]
    generator = UserFlowDiagramGenerator(project_name)
    
    success = generator.generate()
    sys.exit(0 if success else 1)

if __name__ == "__main__":
    main()