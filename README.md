# Patient Record Classifier with Hadoop and Java

## Overview

The Patient Record Classifier is a Java-based application designed to efficiently manage and classify patient records using the Hadoop ecosystem on Ubuntu. It provides functionalities to sort patient data by NHS number, symptoms, and geographical region, leveraging the scalability and distributed computing capabilities of Apache Hadoop.

## Table of Contents

1. [Introduction](#introduction)
2. [Big Data in Healthcare](#big-data-in-healthcare)
   - [Benefits](#benefits)
   - [Challenges](#challenges)
3. [Application Design](#application-design)
   - [Requirements](#requirements)
   - [Considerations](#considerations)
4. [Implementation](#implementation)
   - [Setup](#setup)
   - [Challenges Faced and Solutions Implemented](#challenges-faced-and-solutions-implemented)
5. [Java Program](#java-program)
   - [Functionality](#functionality)
6. [Usage](#usage)
   - [Setup Instructions](#setup-instructions)
   - [Running the Application](#running-the-application)
7. [Results](#results)
8. [Conclusion](#conclusion)

## Introduction

In contemporary healthcare systems, effective management and classification of patient records are essential for optimizing resource allocation and treatment outcomes. This project demonstrates a scalable solution using Java and Hadoop to process and categorize patient data based on unique identifiers, symptoms, and geographical regions.

## Big Data in Healthcare

The healthcare sector benefits immensely from big data analytics, facilitating personalized treatment plans, predictive analytics, and operational efficiencies. However, integrating big data technologies poses challenges such as data privacy, integration complexities, and regulatory compliance.

### Benefits

- **Enhanced Decision-Making:** Utilizing real-time patient data for informed decisions.
- **Personalized Treatment:** Tailoring treatment plans based on comprehensive patient profiles.
- **Data Security:** Implementing robust measures to safeguard patient information.
- **Predictive Analytics:** Forecasting disease outbreaks and trends for proactive healthcare management.
- **Improved Patient Outcomes:** Driving insights from data to enhance healthcare delivery.

### Challenges

- **Data Privacy and Security:** Managing sensitive patient information securely.
- **Data Integration:** Integrating disparate data sources for comprehensive analysis.
- **Regulatory Compliance:** Adhering to healthcare regulations and ethical considerations.
- **Scalability:** Ensuring systems can handle growing volumes of healthcare data.
- **Cost Management:** Balancing investments in technology with healthcare budgets.

## Application Design

### Requirements

This project integrates domain knowledge and research insights to design a robust patient record classification system. It focuses on efficient data handling and processing using Hadoop's distributed computing capabilities.

### Considerations

- **Modularity:** Designing components for reusability and maintainability.
- **Scalability:** Leveraging Hadoop's HDFS and YARN for handling large datasets efficiently.
- **Performance Optimization:** Enhancing system performance through iterative testing and deployment.

## Implementation

### Setup

The project involves setting up Apache Hadoop 3.3.6 on Ubuntu and configuring essential components such as HDFS and YARN. Challenges encountered during setup included permissions management and SSH connectivity.

### Challenges Faced and Solutions Implemented

- Permission Management 
- SSH Configuration 
- System Optimization

## Java Program

### Functionality

The Java program includes functionalities to:
- Load patient records from CSV files into HDFS.
- Classify patient records based on NHS number, symptoms, and regions.
- Implement search functionalities to retrieve specific patient details.

## Usage

### Setup Instructions

1. **Environment Setup:** Install Apache Hadoop 3.3.6 on Ubuntu.
2. **Data Import:** Import patient data into HDFS for processing.
3. **Configuration:** Configure Hadoop's environment variables and system settings.

### Running the Application

1. **Execution:** Compile and run the Java program on the Hadoop cluster.
2. **Search Criteria:** Choose from NHS number, region, or symptom to query patient records.
3. **Output:** View categorized patient details based on the selected criteria.

## Results

The application successfully categorizes and retrieves patient records based on NHS number, symptoms, and regions, showcasing effective utilization of Java and Hadoop for healthcare data management.

## Conclusion

This project highlights the transformative impact of big data technologies on healthcare, emphasizing the importance of data-driven decision-making and personalized patient care. By leveraging Hadoop and Java, the Patient Record Classifier demonstrates a scalable solution for managing and processing vast volumes of patient data efficiently.

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any improvements.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.



