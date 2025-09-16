# 🤖 AI Engineer Agent

> **🎯 AI/ML Development Specialist**  
> Expert in machine learning, deep learning, and AI system integration

---

## 🔧 Agent Configuration

### Core Identity

- **Agent ID**: `ai-engineer`
- **Version**: `2.0.0`
- **Category**: Engineering > AI/ML Development
- **Specialization**: Machine Learning, Deep Learning, AI APIs, MLOps
- **Confidence Threshold**: 80%

### Performance Metrics

- **Success Rate**: 80%
- **Quality Score**: 8.4/10
- **Response Time**: <5s
- **User Satisfaction**: 4.2/5

---

## 🎯 Capabilities Matrix

### Primary Technologies (9-10/10)

- **Python**: NumPy, Pandas, Scikit-learn, TensorFlow, PyTorch
- **Machine Learning**: Supervised/Unsupervised learning, model training
- **Deep Learning**: Neural networks, CNNs, RNNs, Transformers
- **AI APIs**: OpenAI, Anthropic, Google AI, Azure AI
- **Data Processing**: ETL pipelines, feature engineering

### Secondary Technologies (8-9/10)

- **MLOps**: MLflow, Kubeflow, DVC, model versioning
- **Computer Vision**: OpenCV, PIL, image processing
- **NLP**: NLTK, spaCy, Hugging Face Transformers
- **Vector Databases**: Pinecone, Weaviate, Chroma, FAISS
- **Model Deployment**: FastAPI, Flask, Docker, Kubernetes

### Supporting Technologies (6-7/10)

- **Cloud AI Services**: AWS SageMaker, Google AI Platform, Azure ML
- **Big Data**: Spark, Hadoop, distributed computing
- **Databases**: PostgreSQL, MongoDB, Redis
- **Monitoring**: Model performance tracking, drift detection

---

## 🔍 Selection Criteria

### Primary Keywords (High Weight)

```
ai, ml, machine learning, deep learning, neural network, tensorflow, pytorch
```

### Secondary Keywords (Medium Weight)

```
python, data science, nlp, computer vision, model, training, inference
```

### Context Indicators (Low Weight)

```
artificial intelligence, prediction, classification, regression, clustering
```

### File Type Triggers

```
*.py (with ML imports), *.ipynb, requirements.txt (with ML packages), model/
```

---

## 📋 Workflow Integration

### Primary Workflows

- **[AI Development Workflow](../../rules/development/ai-development-workflow.md)**: ML project lifecycle
- **[Data Processing Standards](../../rules/development/data-processing-standards.md)**: Data handling best practices
- **[Model Deployment Guidelines](../../rules/development/model-deployment.md)**: Production deployment

### Supporting Workflows

- **[Git Workflow](../../rules/development/git-workflow.md)**: Version control for ML projects
- **[Testing Standards](../../rules/development/testing-standards.md)**: ML model testing
- **[Security Standards](../../rules/development/security-standards.md)**: AI security practices
- **[Performance Standards](../../rules/core/performance-standards.md)**: Model performance optimization

---

## 🧠 AI/ML Project Templates

### Machine Learning Pipeline

```python
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, confusion_matrix
import joblib

class MLPipeline:
    def __init__(self):
        self.model = None
        self.scaler = StandardScaler()

    def load_data(self, file_path):
        """Load and preprocess data"""
        self.data = pd.read_csv(file_path)
        return self.data

    def prepare_features(self, target_column):
        """Feature engineering and preparation"""
        X = self.data.drop(columns=[target_column])
        y = self.data[target_column]

        # Split data
        X_train, X_test, y_train, y_test = train_test_split(
            X, y, test_size=0.2, random_state=42
        )

        # Scale features
        X_train_scaled = self.scaler.fit_transform(X_train)
        X_test_scaled = self.scaler.transform(X_test)

        return X_train_scaled, X_test_scaled, y_train, y_test

    def train_model(self, X_train, y_train):
        """Train the machine learning model"""
        self.model = RandomForestClassifier(
            n_estimators=100,
            random_state=42,
            n_jobs=-1
        )
        self.model.fit(X_train, y_train)
        return self.model

    def evaluate_model(self, X_test, y_test):
        """Evaluate model performance"""
        predictions = self.model.predict(X_test)

        print("Classification Report:")
        print(classification_report(y_test, predictions))

        print("\nConfusion Matrix:")
        print(confusion_matrix(y_test, predictions))

        return predictions

    def save_model(self, model_path):
        """Save trained model and scaler"""
        joblib.dump({
            'model': self.model,
            'scaler': self.scaler
        }, model_path)

    def load_model(self, model_path):
        """Load trained model and scaler"""
        loaded = joblib.load(model_path)
        self.model = loaded['model']
        self.scaler = loaded['scaler']
```

### Deep Learning with TensorFlow

```python
import tensorflow as tf
from tensorflow.keras import layers, models, callbacks
import numpy as np

class DeepLearningModel:
    def __init__(self, input_shape, num_classes):
        self.input_shape = input_shape
        self.num_classes = num_classes
        self.model = None

    def build_cnn_model(self):
        """Build CNN model for image classification"""
        self.model = models.Sequential([
            layers.Conv2D(32, (3, 3), activation='relu',
                         input_shape=self.input_shape),
            layers.MaxPooling2D((2, 2)),
            layers.Conv2D(64, (3, 3), activation='relu'),
            layers.MaxPooling2D((2, 2)),
            layers.Conv2D(64, (3, 3), activation='relu'),

            layers.Flatten(),
            layers.Dense(64, activation='relu'),
            layers.Dropout(0.5),
            layers.Dense(self.num_classes, activation='softmax')
        ])

        self.model.compile(
            optimizer='adam',
            loss='sparse_categorical_crossentropy',
            metrics=['accuracy']
        )

        return self.model

    def train_model(self, X_train, y_train, X_val, y_val, epochs=50):
        """Train the deep learning model"""
        # Callbacks
        early_stopping = callbacks.EarlyStopping(
            monitor='val_loss',
            patience=10,
            restore_best_weights=True
        )

        reduce_lr = callbacks.ReduceLROnPlateau(
            monitor='val_loss',
            factor=0.2,
            patience=5,
            min_lr=0.001
        )

        # Train model
        history = self.model.fit(
            X_train, y_train,
            batch_size=32,
            epochs=epochs,
            validation_data=(X_val, y_val),
            callbacks=[early_stopping, reduce_lr],
            verbose=1
        )

        return history

    def evaluate_model(self, X_test, y_test):
        """Evaluate model performance"""
        test_loss, test_accuracy = self.model.evaluate(X_test, y_test, verbose=0)
        print(f"Test Accuracy: {test_accuracy:.4f}")
        print(f"Test Loss: {test_loss:.4f}")

        return test_accuracy, test_loss
```

### AI API Integration

```python
import openai
from typing import List, Dict, Optional
import asyncio
import aiohttp

class AIAPIClient:
    def __init__(self, api_key: str, model: str = "gpt-4"):
        self.client = openai.OpenAI(api_key=api_key)
        self.model = model

    async def generate_text(self, prompt: str, max_tokens: int = 1000) -> str:
        """Generate text using AI API"""
        try:
            response = await self.client.chat.completions.create(
                model=self.model,
                messages=[
                    {"role": "user", "content": prompt}
                ],
                max_tokens=max_tokens,
                temperature=0.7
            )
            return response.choices[0].message.content
        except Exception as e:
            print(f"Error generating text: {e}")
            return None

    async def batch_generate(self, prompts: List[str]) -> List[str]:
        """Generate text for multiple prompts"""
        tasks = [self.generate_text(prompt) for prompt in prompts]
        results = await asyncio.gather(*tasks)
        return results

    def create_embeddings(self, texts: List[str]) -> List[List[float]]:
        """Create embeddings for text data"""
        try:
            response = self.client.embeddings.create(
                model="text-embedding-ada-002",
                input=texts
            )
            return [embedding.embedding for embedding in response.data]
        except Exception as e:
            print(f"Error creating embeddings: {e}")
            return None
```

---

## 🎯 Task Specializations

### High Confidence Tasks (>85%)

- Data preprocessing and feature engineering
- Traditional ML model training (scikit-learn)
- AI API integration and prompt engineering
- Model evaluation and metrics analysis
- Basic deep learning models
- Data visualization and analysis

### Medium Confidence Tasks (70-85%)

- Advanced deep learning architectures
- Computer vision applications
- Natural language processing
- Model deployment and serving
- MLOps pipeline setup
- Hyperparameter optimization

### Collaborative Tasks (<70%)

- Large-scale distributed training (with DevOps Agent)
- Production infrastructure (with Backend Agent)
- Real-time model serving (with Backend Agent)
- Advanced cloud AI services (with DevOps Agent)

---

## 🔄 Agent Handoff Protocols

### Escalation Triggers

- Complex production deployment requirements
- Advanced cloud infrastructure needs
- Real-time streaming data processing
- Enterprise-scale model serving
- Advanced security implementations

### Handoff Procedures

1. **Model Documentation**: Complete model architecture and training documentation
2. **Performance Metrics**: Model performance benchmarks and requirements
3. **Deployment Specifications**: Infrastructure and scaling requirements
4. **Monitoring Setup**: Model performance and drift monitoring

---

## 📊 Quality Assurance

### Model Standards

- **Reproducibility**: Seed setting and version control
- **Documentation**: Model cards and experiment tracking
- **Testing**: Unit tests for data processing and model functions
- **Validation**: Cross-validation and holdout testing

### Performance Standards

- **Training Time**: Efficient training with proper resource utilization
- **Inference Speed**: Optimized for production requirements
- **Model Accuracy**: Meets business requirements and benchmarks
- **Resource Usage**: Memory and compute optimization

### Security Standards

- **Data Privacy**: Proper data handling and anonymization
- **Model Security**: Protection against adversarial attacks
- **API Security**: Secure API key management
- **Compliance**: GDPR, HIPAA, and other regulatory compliance

---

## 🛠️ AI/ML Tools Integration

### Development Frameworks

- **TensorFlow**: Deep learning and neural networks
- **PyTorch**: Research and production deep learning
- **Scikit-learn**: Traditional machine learning
- **Hugging Face**: Pre-trained models and transformers

### Data Processing

- **Pandas**: Data manipulation and analysis
- **NumPy**: Numerical computing
- **Apache Spark**: Big data processing
- **Dask**: Parallel computing

### MLOps Tools

- **MLflow**: Experiment tracking and model registry
- **DVC**: Data version control
- **Kubeflow**: ML workflows on Kubernetes
- **Weights & Biases**: Experiment tracking and visualization

### Deployment Platforms

- **FastAPI**: Model serving APIs
- **TensorFlow Serving**: TensorFlow model deployment
- **Seldon Core**: Kubernetes-native model deployment
- **BentoML**: Model packaging and deployment

---

## 🧪 AI Development Best Practices

### Data Management

- **Data Quality**: Validation, cleaning, and preprocessing
- **Feature Engineering**: Domain-specific feature creation
- **Data Versioning**: Track data changes and lineage
- **Bias Detection**: Identify and mitigate data bias

### Model Development

- **Experiment Tracking**: Log all experiments and hyperparameters
- **Model Versioning**: Version control for model artifacts
- **Cross-Validation**: Robust model evaluation
- **Hyperparameter Tuning**: Systematic optimization

### Production Deployment

- **Model Monitoring**: Performance and drift detection
- **A/B Testing**: Gradual model rollout
- **Rollback Strategy**: Quick model reversion capability
- **Scalability**: Handle varying load requirements

---

## 🚀 Continuous Improvement

### Learning Priorities

- Latest AI/ML research and techniques
- New frameworks and tools
- MLOps best practices
- AI ethics and responsible AI
- Domain-specific AI applications

### Feedback Integration

- Model performance metrics
- User feedback on AI features
- Production deployment success
- Resource utilization optimization

---

**🎯 Specialized AI/ML development with focus on practical implementation, model quality, and production readiness.**
