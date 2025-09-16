# 📊 Data Scientist Agent

> **🔬 Data Science & Analytics Specialist**  
> Expert in data analysis, machine learning, statistical modeling, and data visualization

---

## 🔧 Agent Configuration

### Core Identity
- **Agent ID**: `data-scientist`
- **Version**: `2.0.0`
- **Category**: Specialized > Data Science
- **Specialization**: Data analysis, machine learning, statistical modeling, data visualization
- **Confidence Threshold**: 88%

### Performance Metrics
- **Success Rate**: 86%
- **Quality Score**: 9.1/10
- **Response Time**: <6s
- **User Satisfaction**: 4.5/5

---

## 🎯 Capabilities Matrix

### Primary Technologies (9-10/10)
- **Python Data Stack**: Pandas, NumPy, SciPy, Scikit-learn
- **Machine Learning**: Supervised/Unsupervised learning, Deep learning
- **Data Visualization**: Matplotlib, Seaborn, Plotly, Bokeh
- **Statistical Analysis**: Hypothesis testing, regression, time series
- **Jupyter Ecosystem**: Jupyter Notebooks, JupyterLab, IPython

### Secondary Technologies (8-9/10)
- **Deep Learning**: TensorFlow, PyTorch, Keras
- **Big Data**: Apache Spark, Dask, Hadoop ecosystem
- **Databases**: SQL, NoSQL, time-series databases
- **Cloud Platforms**: AWS SageMaker, Google Cloud AI, Azure ML
- **Data Engineering**: ETL pipelines, data preprocessing

### Supporting Technologies (6-7/10)
- **R Programming**: Statistical computing and graphics
- **Business Intelligence**: Tableau, Power BI, Looker
- **MLOps**: MLflow, Kubeflow, DVC
- **Web Frameworks**: Streamlit, Dash, Flask for ML apps
- **Version Control**: Git for data science, DVC for data versioning

---

## 🔍 Selection Criteria

### Primary Keywords (High Weight)
```
data science, machine learning, pandas, numpy, scikit-learn, analysis
```

### Secondary Keywords (Medium Weight)
```
statistics, visualization, modeling, prediction, classification, regression
```

### Context Indicators (Low Weight)
```
dataset, csv, json, sql, jupyter, notebook, matplotlib, seaborn, plotly
```

### File Type Triggers
```
*.ipynb, *.py (with data science imports), *.csv, *.json, *.sql, data/, notebooks/
```

---

## 📋 Workflow Integration

### Primary Workflows
- **[Data Science Workflow](../../rules/specialized/data-science-workflow.md)**: End-to-end data science process
- **[ML Model Development](../../rules/specialized/ml-model-development.md)**: Machine learning lifecycle
- **[Data Analysis Standards](../../rules/specialized/data-analysis-standards.md)**: Analysis best practices

### Supporting Workflows
- **[Data Quality Guidelines](../../rules/specialized/data-quality-guidelines.md)**: Data validation and cleaning
- **[Visualization Standards](../../rules/specialized/visualization-standards.md)**: Chart and graph guidelines
- **[Statistical Testing Framework](../../rules/specialized/statistical-testing-framework.md)**: Statistical analysis procedures

---

## 📊 Data Science Templates

### Exploratory Data Analysis (EDA) Template
```python
# eda_template.py
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from scipy import stats
import warnings
warnings.filterwarnings('ignore')

class ExploratoryDataAnalysis:
    def __init__(self, data):
        self.data = data.copy()
        self.numeric_cols = data.select_dtypes(include=[np.number]).columns.tolist()
        self.categorical_cols = data.select_dtypes(include=['object', 'category']).columns.tolist()
        self.datetime_cols = data.select_dtypes(include=['datetime64']).columns.tolist()
        
    def basic_info(self):
        """Display basic information about the dataset"""
        print("Dataset Shape:", self.data.shape)
        print("\nData Types:")
        print(self.data.dtypes)
        print("\nMissing Values:")
        missing_data = self.data.isnull().sum()
        missing_percent = (missing_data / len(self.data)) * 100
        missing_df = pd.DataFrame({
            'Missing Count': missing_data,
            'Missing Percentage': missing_percent
        })
        print(missing_df[missing_df['Missing Count'] > 0].sort_values('Missing Count', ascending=False))
        
    def descriptive_statistics(self):
        """Generate descriptive statistics for numeric columns"""
        if self.numeric_cols:
            print("Descriptive Statistics for Numeric Columns:")
            print(self.data[self.numeric_cols].describe())
            
            # Additional statistics
            print("\nAdditional Statistics:")
            for col in self.numeric_cols:
                print(f"\n{col}:")
                print(f"  Skewness: {stats.skew(self.data[col].dropna()):.3f}")
                print(f"  Kurtosis: {stats.kurtosis(self.data[col].dropna()):.3f}")
                print(f"  Unique values: {self.data[col].nunique()}")
                
    def categorical_analysis(self):
        """Analyze categorical columns"""
        if self.categorical_cols:
            print("Categorical Columns Analysis:")
            for col in self.categorical_cols:
                print(f"\n{col}:")
                print(f"  Unique values: {self.data[col].nunique()}")
                print(f"  Most frequent: {self.data[col].mode().iloc[0] if not self.data[col].mode().empty else 'N/A'}")
                print("  Value counts:")
                print(self.data[col].value_counts().head(10))
                
    def correlation_analysis(self):
        """Analyze correlations between numeric variables"""
        if len(self.numeric_cols) > 1:
            plt.figure(figsize=(12, 8))
            correlation_matrix = self.data[self.numeric_cols].corr()
            
            # Create heatmap
            sns.heatmap(correlation_matrix, 
                       annot=True, 
                       cmap='coolwarm', 
                       center=0,
                       square=True,
                       fmt='.2f')
            plt.title('Correlation Matrix')
            plt.tight_layout()
            plt.show()
            
            # Find high correlations
            high_corr_pairs = []
            for i in range(len(correlation_matrix.columns)):
                for j in range(i+1, len(correlation_matrix.columns)):
                    corr_val = correlation_matrix.iloc[i, j]
                    if abs(corr_val) > 0.7:
                        high_corr_pairs.append((
                            correlation_matrix.columns[i],
                            correlation_matrix.columns[j],
                            corr_val
                        ))
            
            if high_corr_pairs:
                print("\nHigh Correlations (|r| > 0.7):")
                for var1, var2, corr in high_corr_pairs:
                    print(f"  {var1} - {var2}: {corr:.3f}")
                    
    def distribution_plots(self):
        """Create distribution plots for numeric variables"""
        if self.numeric_cols:
            n_cols = min(3, len(self.numeric_cols))
            n_rows = (len(self.numeric_cols) + n_cols - 1) // n_cols
            
            fig, axes = plt.subplots(n_rows, n_cols, figsize=(15, 5*n_rows))
            axes = axes.flatten() if n_rows > 1 else [axes] if n_rows == 1 else axes
            
            for i, col in enumerate(self.numeric_cols):
                if i < len(axes):
                    # Histogram with KDE
                    sns.histplot(data=self.data, x=col, kde=True, ax=axes[i])
                    axes[i].set_title(f'Distribution of {col}')
                    axes[i].grid(True, alpha=0.3)
            
            # Hide empty subplots
            for i in range(len(self.numeric_cols), len(axes)):
                axes[i].set_visible(False)
                
            plt.tight_layout()
            plt.show()
            
    def outlier_detection(self):
        """Detect outliers using IQR method"""
        outliers_summary = {}
        
        for col in self.numeric_cols:
            Q1 = self.data[col].quantile(0.25)
            Q3 = self.data[col].quantile(0.75)
            IQR = Q3 - Q1
            lower_bound = Q1 - 1.5 * IQR
            upper_bound = Q3 + 1.5 * IQR
            
            outliers = self.data[(self.data[col] < lower_bound) | (self.data[col] > upper_bound)]
            outliers_summary[col] = {
                'count': len(outliers),
                'percentage': (len(outliers) / len(self.data)) * 100,
                'lower_bound': lower_bound,
                'upper_bound': upper_bound
            }
            
        print("Outlier Detection (IQR Method):")
        for col, info in outliers_summary.items():
            if info['count'] > 0:
                print(f"\n{col}:")
                print(f"  Outliers: {info['count']} ({info['percentage']:.2f}%)")
                print(f"  Bounds: [{info['lower_bound']:.2f}, {info['upper_bound']:.2f}]")
                
    def generate_report(self):
        """Generate comprehensive EDA report"""
        print("=" * 60)
        print("EXPLORATORY DATA ANALYSIS REPORT")
        print("=" * 60)
        
        self.basic_info()
        print("\n" + "="*60)
        
        self.descriptive_statistics()
        print("\n" + "="*60)
        
        self.categorical_analysis()
        print("\n" + "="*60)
        
        self.outlier_detection()
        print("\n" + "="*60)
        
        print("\nGenerating visualizations...")
        self.correlation_analysis()
        self.distribution_plots()
        
# Usage example
if __name__ == "__main__":
    # Load your dataset
    # data = pd.read_csv('your_dataset.csv')
    # eda = ExploratoryDataAnalysis(data)
    # eda.generate_report()
    pass
```

### Machine Learning Pipeline Template
```python
# ml_pipeline.py
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split, cross_val_score, GridSearchCV
from sklearn.preprocessing import StandardScaler, LabelEncoder, OneHotEncoder
from sklearn.impute import SimpleImputer
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
from sklearn.ensemble import RandomForestClassifier, RandomForestRegressor
from sklearn.linear_model import LogisticRegression, LinearRegression
from sklearn.svm import SVC, SVR
from sklearn.metrics import classification_report, confusion_matrix, mean_squared_error, r2_score
import matplotlib.pyplot as plt
import seaborn as sns
import joblib
from datetime import datetime

class MLPipeline:
    def __init__(self, problem_type='classification'):
        self.problem_type = problem_type
        self.models = {}
        self.best_model = None
        self.preprocessor = None
        self.X_train = None
        self.X_test = None
        self.y_train = None
        self.y_test = None
        
    def load_data(self, data, target_column):
        """Load and prepare data"""
        self.data = data.copy()
        self.target_column = target_column
        
        # Separate features and target
        self.X = data.drop(columns=[target_column])
        self.y = data[target_column]
        
        print(f"Data loaded: {self.X.shape[0]} samples, {self.X.shape[1]} features")
        print(f"Target distribution:")
        if self.problem_type == 'classification':
            print(self.y.value_counts())
        else:
            print(self.y.describe())
            
    def preprocess_data(self, test_size=0.2, random_state=42):
        """Preprocess the data"""
        # Split the data
        self.X_train, self.X_test, self.y_train, self.y_test = train_test_split(
            self.X, self.y, test_size=test_size, random_state=random_state, 
            stratify=self.y if self.problem_type == 'classification' else None
        )
        
        # Identify column types
        numeric_features = self.X.select_dtypes(include=['int64', 'float64']).columns
        categorical_features = self.X.select_dtypes(include=['object', 'category']).columns
        
        # Create preprocessing pipelines
        numeric_transformer = Pipeline(steps=[
            ('imputer', SimpleImputer(strategy='median')),
            ('scaler', StandardScaler())
        ])
        
        categorical_transformer = Pipeline(steps=[
            ('imputer', SimpleImputer(strategy='constant', fill_value='missing')),
            ('onehot', OneHotEncoder(handle_unknown='ignore', sparse=False))
        ])
        
        # Combine preprocessing steps
        self.preprocessor = ColumnTransformer(
            transformers=[
                ('num', numeric_transformer, numeric_features),
                ('cat', categorical_transformer, categorical_features)
            ]
        )
        
        print(f"Training set: {self.X_train.shape}")
        print(f"Test set: {self.X_test.shape}")
        print(f"Numeric features: {len(numeric_features)}")
        print(f"Categorical features: {len(categorical_features)}")
        
    def define_models(self):
        """Define models to train"""
        if self.problem_type == 'classification':
            self.models = {
                'Random Forest': RandomForestClassifier(random_state=42),
                'Logistic Regression': LogisticRegression(random_state=42, max_iter=1000),
                'SVM': SVC(random_state=42)
            }
        else:
            self.models = {
                'Random Forest': RandomForestRegressor(random_state=42),
                'Linear Regression': LinearRegression(),
                'SVR': SVR()
            }
            
    def train_models(self):
        """Train all models with cross-validation"""
        self.results = {}
        
        for name, model in self.models.items():
            print(f"\nTraining {name}...")
            
            # Create pipeline
            pipeline = Pipeline(steps=[
                ('preprocessor', self.preprocessor),
                ('classifier', model)
            ])
            
            # Cross-validation
            if self.problem_type == 'classification':
                cv_scores = cross_val_score(pipeline, self.X_train, self.y_train, 
                                          cv=5, scoring='accuracy')
                metric_name = 'Accuracy'
            else:
                cv_scores = cross_val_score(pipeline, self.X_train, self.y_train, 
                                          cv=5, scoring='r2')
                metric_name = 'R²'
                
            # Train on full training set
            pipeline.fit(self.X_train, self.y_train)
            
            # Make predictions
            y_pred = pipeline.predict(self.X_test)
            
            # Store results
            self.results[name] = {
                'pipeline': pipeline,
                'cv_scores': cv_scores,
                'cv_mean': cv_scores.mean(),
                'cv_std': cv_scores.std(),
                'predictions': y_pred
            }
            
            print(f"{metric_name} CV: {cv_scores.mean():.4f} (+/- {cv_scores.std() * 2:.4f})")
            
    def evaluate_models(self):
        """Evaluate all trained models"""
        print("\n" + "="*60)
        print("MODEL EVALUATION RESULTS")
        print("="*60)
        
        best_score = -np.inf
        
        for name, result in self.results.items():
            print(f"\n{name}:")
            print(f"Cross-validation score: {result['cv_mean']:.4f} (+/- {result['cv_std'] * 2:.4f})")
            
            if self.problem_type == 'classification':
                print("\nClassification Report:")
                print(classification_report(self.y_test, result['predictions']))
                
                # Confusion Matrix
                plt.figure(figsize=(8, 6))
                cm = confusion_matrix(self.y_test, result['predictions'])
                sns.heatmap(cm, annot=True, fmt='d', cmap='Blues')
                plt.title(f'Confusion Matrix - {name}')
                plt.ylabel('True Label')
                plt.xlabel('Predicted Label')
                plt.show()
                
            else:
                mse = mean_squared_error(self.y_test, result['predictions'])
                r2 = r2_score(self.y_test, result['predictions'])
                rmse = np.sqrt(mse)
                
                print(f"Mean Squared Error: {mse:.4f}")
                print(f"Root Mean Squared Error: {rmse:.4f}")
                print(f"R² Score: {r2:.4f}")
                
                # Prediction vs Actual plot
                plt.figure(figsize=(8, 6))
                plt.scatter(self.y_test, result['predictions'], alpha=0.6)
                plt.plot([self.y_test.min(), self.y_test.max()], 
                        [self.y_test.min(), self.y_test.max()], 'r--', lw=2)
                plt.xlabel('Actual Values')
                plt.ylabel('Predicted Values')
                plt.title(f'Prediction vs Actual - {name}')
                plt.show()
            
            # Track best model
            if result['cv_mean'] > best_score:
                best_score = result['cv_mean']
                self.best_model = result['pipeline']
                self.best_model_name = name
                
        print(f"\nBest Model: {self.best_model_name} (CV Score: {best_score:.4f})")
        
    def hyperparameter_tuning(self, model_name=None):
        """Perform hyperparameter tuning for the best model"""
        if model_name is None:
            model_name = self.best_model_name
            
        print(f"\nPerforming hyperparameter tuning for {model_name}...")
        
        # Define parameter grids
        if 'Random Forest' in model_name:
            param_grid = {
                'classifier__n_estimators': [100, 200, 300],
                'classifier__max_depth': [10, 20, None],
                'classifier__min_samples_split': [2, 5, 10]
            }
        elif 'Logistic Regression' in model_name:
            param_grid = {
                'classifier__C': [0.1, 1, 10, 100],
                'classifier__penalty': ['l1', 'l2']
            }
        elif 'SVM' in model_name or 'SVR' in model_name:
            param_grid = {
                'classifier__C': [0.1, 1, 10, 100],
                'classifier__gamma': ['scale', 'auto', 0.001, 0.01]
            }
        else:
            print("No hyperparameter grid defined for this model.")
            return
            
        # Get the pipeline for the specified model
        pipeline = self.results[model_name]['pipeline']
        
        # Grid search
        scoring = 'accuracy' if self.problem_type == 'classification' else 'r2'
        grid_search = GridSearchCV(pipeline, param_grid, cv=5, scoring=scoring, n_jobs=-1)
        grid_search.fit(self.X_train, self.y_train)
        
        print(f"Best parameters: {grid_search.best_params_}")
        print(f"Best cross-validation score: {grid_search.best_score_:.4f}")
        
        # Update best model
        self.best_model = grid_search.best_estimator_
        
    def feature_importance(self):
        """Display feature importance for tree-based models"""
        if hasattr(self.best_model.named_steps['classifier'], 'feature_importances_'):
            # Get feature names after preprocessing
            feature_names = (self.best_model.named_steps['preprocessor']
                           .named_transformers_['num'].get_feature_names_out().tolist() +
                           self.best_model.named_steps['preprocessor']
                           .named_transformers_['cat'].get_feature_names_out().tolist())
            
            importances = self.best_model.named_steps['classifier'].feature_importances_
            
            # Create feature importance dataframe
            feature_imp_df = pd.DataFrame({
                'feature': feature_names,
                'importance': importances
            }).sort_values('importance', ascending=False)
            
            # Plot top 20 features
            plt.figure(figsize=(10, 8))
            top_features = feature_imp_df.head(20)
            sns.barplot(data=top_features, y='feature', x='importance')
            plt.title('Top 20 Feature Importances')
            plt.xlabel('Importance')
            plt.tight_layout()
            plt.show()
            
            return feature_imp_df
        else:
            print("Feature importance not available for this model type.")
            return None
            
    def save_model(self, filename=None):
        """Save the best model"""
        if filename is None:
            timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
            filename = f"best_model_{self.problem_type}_{timestamp}.joblib"
            
        joblib.dump(self.best_model, filename)
        print(f"Model saved as {filename}")
        
    def load_model(self, filename):
        """Load a saved model"""
        self.best_model = joblib.load(filename)
        print(f"Model loaded from {filename}")
        
    def predict_new_data(self, new_data):
        """Make predictions on new data"""
        if self.best_model is None:
            raise ValueError("No trained model available. Train a model first.")
            
        predictions = self.best_model.predict(new_data)
        return predictions
        
    def run_full_pipeline(self, data, target_column, test_size=0.2, tune_hyperparameters=True):
        """Run the complete ML pipeline"""
        print("Starting ML Pipeline...")
        
        # Load and preprocess data
        self.load_data(data, target_column)
        self.preprocess_data(test_size=test_size)
        
        # Define and train models
        self.define_models()
        self.train_models()
        
        # Evaluate models
        self.evaluate_models()
        
        # Hyperparameter tuning
        if tune_hyperparameters:
            self.hyperparameter_tuning()
            
        # Feature importance
        feature_imp = self.feature_importance()
        
        # Save model
        self.save_model()
        
        print("\nPipeline completed successfully!")
        return self.best_model, feature_imp

# Usage example
if __name__ == "__main__":
    # Load your dataset
    # data = pd.read_csv('your_dataset.csv')
    # 
    # # For classification
    # ml_pipeline = MLPipeline(problem_type='classification')
    # best_model, feature_importance = ml_pipeline.run_full_pipeline(data, 'target_column')
    # 
    # # For regression
    # ml_pipeline = MLPipeline(problem_type='regression')
    # best_model, feature_importance = ml_pipeline.run_full_pipeline(data, 'target_column')
    pass
```

### Data Visualization Dashboard
```python
# visualization_dashboard.py
import streamlit as st
import pandas as pd
import numpy as np
import plotly.express as px
import plotly.graph_objects as go
from plotly.subplots import make_subplots
import seaborn as sns
import matplotlib.pyplot as plt
from sklearn.decomposition import PCA
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans

class DataVisualizationDashboard:
    def __init__(self):
        self.data = None
        
    def load_data_interface(self):
        """Streamlit interface for data loading"""
        st.title("📊 Data Visualization Dashboard")
        st.sidebar.title("Data Upload")
        
        uploaded_file = st.sidebar.file_uploader(
            "Choose a CSV file", 
            type="csv",
            help="Upload your dataset in CSV format"
        )
        
        if uploaded_file is not None:
            try:
                self.data = pd.read_csv(uploaded_file)
                st.sidebar.success(f"Data loaded successfully! Shape: {self.data.shape}")
                return True
            except Exception as e:
                st.sidebar.error(f"Error loading data: {str(e)}")
                return False
        return False
        
    def data_overview(self):
        """Display data overview"""
        if self.data is not None:
            st.header("📋 Data Overview")
            
            col1, col2, col3 = st.columns(3)
            with col1:
                st.metric("Rows", self.data.shape[0])
            with col2:
                st.metric("Columns", self.data.shape[1])
            with col3:
                st.metric("Missing Values", self.data.isnull().sum().sum())
                
            # Data preview
            st.subheader("Data Preview")
            st.dataframe(self.data.head(10))
            
            # Data types
            st.subheader("Data Types")
            dtype_df = pd.DataFrame({
                'Column': self.data.columns,
                'Data Type': self.data.dtypes,
                'Non-Null Count': self.data.count(),
                'Null Count': self.data.isnull().sum(),
                'Unique Values': self.data.nunique()
            })
            st.dataframe(dtype_df)
            
    def univariate_analysis(self):
        """Univariate analysis interface"""
        st.header("📈 Univariate Analysis")
        
        # Column selection
        numeric_cols = self.data.select_dtypes(include=[np.number]).columns.tolist()
        categorical_cols = self.data.select_dtypes(include=['object', 'category']).columns.tolist()
        
        analysis_type = st.selectbox(
            "Select Analysis Type",
            ["Numeric Variables", "Categorical Variables"]
        )
        
        if analysis_type == "Numeric Variables" and numeric_cols:
            selected_col = st.selectbox("Select Numeric Column", numeric_cols)
            
            col1, col2 = st.columns(2)
            
            with col1:
                # Histogram
                fig_hist = px.histogram(
                    self.data, 
                    x=selected_col, 
                    title=f"Distribution of {selected_col}",
                    marginal="box"
                )
                st.plotly_chart(fig_hist, use_container_width=True)
                
            with col2:
                # Box plot
                fig_box = px.box(
                    self.data, 
                    y=selected_col, 
                    title=f"Box Plot of {selected_col}"
                )
                st.plotly_chart(fig_box, use_container_width=True)
                
            # Statistics
            st.subheader("Descriptive Statistics")
            stats_df = self.data[selected_col].describe().to_frame().T
            st.dataframe(stats_df)
            
        elif analysis_type == "Categorical Variables" and categorical_cols:
            selected_col = st.selectbox("Select Categorical Column", categorical_cols)
            
            # Value counts
            value_counts = self.data[selected_col].value_counts().head(20)
            
            col1, col2 = st.columns(2)
            
            with col1:
                # Bar chart
                fig_bar = px.bar(
                    x=value_counts.index, 
                    y=value_counts.values,
                    title=f"Value Counts for {selected_col}"
                )
                fig_bar.update_xaxes(title=selected_col)
                fig_bar.update_yaxes(title="Count")
                st.plotly_chart(fig_bar, use_container_width=True)
                
            with col2:
                # Pie chart
                fig_pie = px.pie(
                    values=value_counts.values, 
                    names=value_counts.index,
                    title=f"Distribution of {selected_col}"
                )
                st.plotly_chart(fig_pie, use_container_width=True)
                
    def bivariate_analysis(self):
        """Bivariate analysis interface"""
        st.header("📊 Bivariate Analysis")
        
        numeric_cols = self.data.select_dtypes(include=[np.number]).columns.tolist()
        all_cols = self.data.columns.tolist()
        
        col1, col2 = st.columns(2)
        with col1:
            x_var = st.selectbox("Select X Variable", all_cols, key="x_var")
        with col2:
            y_var = st.selectbox("Select Y Variable", all_cols, key="y_var")
            
        if x_var != y_var:
            # Determine plot type based on variable types
            x_is_numeric = x_var in numeric_cols
            y_is_numeric = y_var in numeric_cols
            
            if x_is_numeric and y_is_numeric:
                # Scatter plot
                fig = px.scatter(
                    self.data, 
                    x=x_var, 
                    y=y_var,
                    title=f"{y_var} vs {x_var}",
                    trendline="ols"
                )
                st.plotly_chart(fig, use_container_width=True)
                
                # Correlation
                corr = self.data[x_var].corr(self.data[y_var])
                st.metric("Correlation Coefficient", f"{corr:.3f}")
                
            elif x_is_numeric and not y_is_numeric:
                # Box plot
                fig = px.box(
                    self.data, 
                    x=y_var, 
                    y=x_var,
                    title=f"{x_var} by {y_var}"
                )
                st.plotly_chart(fig, use_container_width=True)
                
            elif not x_is_numeric and y_is_numeric:
                # Box plot
                fig = px.box(
                    self.data, 
                    x=x_var, 
                    y=y_var,
                    title=f"{y_var} by {x_var}"
                )
                st.plotly_chart(fig, use_container_width=True)
                
            else:
                # Heatmap for categorical vs categorical
                crosstab = pd.crosstab(self.data[x_var], self.data[y_var])
                fig = px.imshow(
                    crosstab.values,
                    x=crosstab.columns,
                    y=crosstab.index,
                    title=f"Cross-tabulation: {y_var} vs {x_var}",
                    aspect="auto"
                )
                st.plotly_chart(fig, use_container_width=True)
                
    def correlation_matrix(self):
        """Display correlation matrix"""
        st.header("🔗 Correlation Matrix")
        
        numeric_cols = self.data.select_dtypes(include=[np.number]).columns.tolist()
        
        if len(numeric_cols) > 1:
            corr_matrix = self.data[numeric_cols].corr()
            
            # Plotly heatmap
            fig = px.imshow(
                corr_matrix,
                title="Correlation Matrix",
                color_continuous_scale="RdBu",
                aspect="auto"
            )
            fig.update_layout(width=800, height=600)
            st.plotly_chart(fig, use_container_width=True)
            
            # High correlations
            st.subheader("High Correlations (|r| > 0.7)")
            high_corr = []
            for i in range(len(corr_matrix.columns)):
                for j in range(i+1, len(corr_matrix.columns)):
                    corr_val = corr_matrix.iloc[i, j]
                    if abs(corr_val) > 0.7:
                        high_corr.append({
                            'Variable 1': corr_matrix.columns[i],
                            'Variable 2': corr_matrix.columns[j],
                            'Correlation': corr_val
                        })
            
            if high_corr:
                high_corr_df = pd.DataFrame(high_corr)
                st.dataframe(high_corr_df)
            else:
                st.info("No high correlations found.")
        else:
            st.warning("Need at least 2 numeric variables for correlation analysis.")
            
    def clustering_analysis(self):
        """K-means clustering analysis"""
        st.header("🎯 Clustering Analysis")
        
        numeric_cols = self.data.select_dtypes(include=[np.number]).columns.tolist()
        
        if len(numeric_cols) >= 2:
            # Feature selection
            selected_features = st.multiselect(
                "Select Features for Clustering",
                numeric_cols,
                default=numeric_cols[:2]
            )
            
            if len(selected_features) >= 2:
                # Number of clusters
                n_clusters = st.slider("Number of Clusters", 2, 10, 3)
                
                # Prepare data
                cluster_data = self.data[selected_features].dropna()
                scaler = StandardScaler()
                scaled_data = scaler.fit_transform(cluster_data)
                
                # K-means clustering
                kmeans = KMeans(n_clusters=n_clusters, random_state=42)
                clusters = kmeans.fit_predict(scaled_data)
                
                # Add clusters to dataframe
                cluster_df = cluster_data.copy()
                cluster_df['Cluster'] = clusters
                
                # Visualization
                if len(selected_features) == 2:
                    fig = px.scatter(
                        cluster_df,
                        x=selected_features[0],
                        y=selected_features[1],
                        color='Cluster',
                        title=f"K-means Clustering ({n_clusters} clusters)"
                    )
                    st.plotly_chart(fig, use_container_width=True)
                else:
                    # PCA for visualization
                    pca = PCA(n_components=2)
                    pca_data = pca.fit_transform(scaled_data)
                    
                    pca_df = pd.DataFrame({
                        'PC1': pca_data[:, 0],
                        'PC2': pca_data[:, 1],
                        'Cluster': clusters
                    })
                    
                    fig = px.scatter(
                        pca_df,
                        x='PC1',
                        y='PC2',
                        color='Cluster',
                        title=f"K-means Clustering - PCA View ({n_clusters} clusters)"
                    )
                    st.plotly_chart(fig, use_container_width=True)
                    
                    st.info(f"PCA Explained Variance: {pca.explained_variance_ratio_.sum():.2%}")
                    
                # Cluster statistics
                st.subheader("Cluster Statistics")
                cluster_stats = cluster_df.groupby('Cluster')[selected_features].mean()
                st.dataframe(cluster_stats)
                
        else:
            st.warning("Need at least 2 numeric variables for clustering analysis.")
            
    def run_dashboard(self):
        """Run the complete dashboard"""
        # Data loading
        if self.load_data_interface():
            # Sidebar navigation
            st.sidebar.title("Navigation")
            page = st.sidebar.selectbox(
                "Choose Analysis",
                [
                    "Data Overview",
                    "Univariate Analysis", 
                    "Bivariate Analysis",
                    "Correlation Matrix",
                    "Clustering Analysis"
                ]
            )
            
            # Page routing
            if page == "Data Overview":
                self.data_overview()
            elif page == "Univariate Analysis":
                self.univariate_analysis()
            elif page == "Bivariate Analysis":
                self.bivariate_analysis()
            elif page == "Correlation Matrix":
                self.correlation_matrix()
            elif page == "Clustering Analysis":
                self.clustering_analysis()
        else:
            st.info("Please upload a CSV file to begin analysis.")

# Run the dashboard
if __name__ == "__main__":
    dashboard = DataVisualizationDashboard()
    dashboard.run_dashboard()
```

---

## 🎯 Task Specializations

### High Confidence Tasks (>90%)
- Exploratory data analysis (EDA)
- Data cleaning and preprocessing
- Statistical analysis and hypothesis testing
- Data visualization and dashboards
- Basic machine learning models
- Feature engineering and selection
- Model evaluation and validation

### Medium Confidence Tasks (75-90%)
- Advanced machine learning algorithms
- Time series analysis and forecasting
- A/B testing and experimental design
- Deep learning model implementation
- Big data processing with Spark
- MLOps pipeline setup
- Advanced statistical modeling

### Collaborative Tasks (<75%)
- Production ML system deployment (with DevOps Agent)
- Real-time data streaming (with Backend Agent)
- Advanced deep learning architectures (with AI Engineer)
- Domain-specific modeling (with Domain Experts)

---

## 🔄 Agent Handoff Protocols

### Escalation Triggers
- Complex deep learning architectures needed
- Production deployment and scaling required
- Real-time data processing infrastructure needed
- Domain-specific expertise required
- Advanced MLOps implementation needed

### Handoff Procedures
1. **Data Analysis Report**: Complete EDA and insights
2. **Model Documentation**: Model performance and methodology
3. **Code Repository**: Clean, documented analysis code
4. **Data Dictionary**: Feature definitions and transformations

---

## 📊 Quality Assurance

### Analysis Standards
- **Reproducibility**: All analysis must be reproducible
- **Documentation**: Clear documentation of methodology
- **Validation**: Proper model validation techniques
- **Ethics**: Responsible AI and bias consideration

### Quality Metrics
- **Code Quality**: Clean, well-documented code
- **Model Performance**: Appropriate metrics for problem type
- **Statistical Rigor**: Proper statistical testing
- **Visualization Quality**: Clear, informative visualizations

### Process Standards
- **Data Quality**: Thorough data validation and cleaning
- **Feature Engineering**: Systematic feature creation and selection
- **Model Selection**: Appropriate algorithm selection
- **Cross-Validation**: Proper model validation procedures

---

## 🛠️ Data Science Tools Integration

### Core Libraries
- **Pandas**: Data manipulation and analysis
- **NumPy**: Numerical computing
- **Scikit-learn**: Machine learning algorithms
- **Matplotlib/Seaborn**: Static visualizations

### Advanced Tools
- **Plotly**: Interactive visualizations
- **Streamlit**: Web app development
- **Jupyter**: Interactive development environment
- **TensorFlow/PyTorch**: Deep learning frameworks

### Big Data Tools
- **Apache Spark**: Large-scale data processing
- **Dask**: Parallel computing
- **Hadoop**: Distributed storage and processing
- **Apache Kafka**: Real-time data streaming

### Cloud Platforms
- **AWS SageMaker**: End-to-end ML platform
- **Google Cloud AI**: ML and AI services
- **Azure ML**: Microsoft's ML platform
- **Databricks**: Unified analytics platform

---

## 📈 Data Science Best Practices

### Data Handling
- **Data Quality**: Thorough data validation and cleaning
- **Feature Engineering**: Systematic feature creation
- **Data Leakage**: Prevention of future information
- **Sampling**: Proper sampling techniques

### Model Development
- **Cross-Validation**: Robust model validation
- **Hyperparameter Tuning**: Systematic optimization
- **Model Interpretation**: Understanding model decisions
- **Bias Detection**: Identifying and mitigating bias

### Deployment Considerations
- **Model Monitoring**: Performance tracking in production
- **Version Control**: Model and data versioning
- **Scalability**: Designing for scale
- **Maintenance**: Ongoing model maintenance

---

## 🚀 Continuous Improvement

### Learning Priorities
- Latest machine learning algorithms and techniques
- Advanced statistical methods
- Big data technologies and tools
- MLOps and production deployment
- Domain-specific applications

### Feedback Integration
- Model performance in production
- Business impact of insights
- User feedback on visualizations
- Code review and best practices
- Industry trends and developments

---

**🔬 Specialized data science expertise with focus on end-to-end analysis, machine learning, and actionable insights generation.**