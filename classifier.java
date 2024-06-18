// Importing libraries
// To manage configuration settings.
import org.apache.hadoop.conf.Configuration;
//To interact with the file system.
import org.apache.hadoop.fs.FileSystem;
// To represent file path.
import org.apache.hadoop.fs.Path;
// To read text data from input file
import java.io.BufferedReader; 
// To read content from file in the java program
import java.io.FileReader;
// To handle I/O exceptions
import java.io.IOException;
// To store collection of elements
import java.util.ArrayList;
// To store key value pairs
import java.util.HashMap;
 // To store multiple elements in a sequence
import java.util.List;
// Similar to HashMap to store key value pairs, retrieve and modify elements based on keys
import java.util.Map;
// To help in reading different types of data format
import java.util.Scanner;


public class classifier {
    // Defining constants for column indices in the CSV file
    private static final int NHS_NUMBER_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int AGE_INDEX = 2;
    private static final int GENDER_INDEX = 3;
    private static final int DATE_OF_ADMISSION_INDEX = 4;
    private static final int MEDICAL_RECORD_NUMBER_INDEX = 5;
    private static final int MEDICAL_HISTORY_INDEX = 6;
    private static final int CHIEF_COMPLAINT_INDEX = 7;
    private static final int HISTORY_OF_PRESENT_ILLNESS_INDEX = 8;
    private static final int PHYSICAL_EXAMINATION_INDEX = 9;
    private static final int ASSESSMENT_AND_PLAN_INDEX = 10;
    private static final int NHS_TRUST_REGION_INDEX = 11;

    // Defining data structures to store patient records
    private List<String[]> patientRecords; // Listing of patient records
    private Map<String, List<String[]>> recordsByMedicalHistory; // Mapping of symptoms 
    private Map<String, List<String[]>> recordsByRegion; // Mapping of regions 


    public classifier() {
        patientRecords = new ArrayList<>();  // This list will store arrays of patient records.
        recordsByMedicalHistory = new HashMap<>(); // This map will store patient records categorized by medical history.
        recordsByRegion = new HashMap<>(); // This map will store patient records categorized by NHS trust region.
    }
    // Loading patient records from the CSV file
    public void loadPatientRecords(String csvFilePath) throws IOException {
        // To close the statement after the data is read from the csv file
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            // To store each line read from the file
            String line;
            br.readLine(); // To skip header line
            // To read each line until there are no more
            while ((line = br.readLine()) != null) {
                // To split lines into individual elements based on commas
                List<String> record = parseCSVLine(line);

                // Combining DATE_OF_ADMISSION and MEDICAL_RECORD_NUMBER values
                String dateOfAdmission = record.get(DATE_OF_ADMISSION_INDEX) + "," + record.get(MEDICAL_RECORD_NUMBER_INDEX);
                record.set(DATE_OF_ADMISSION_INDEX, dateOfAdmission);

                // Moving contents from MEDICAL_HISTORY to MEDICAL_RECORD_NUMBER
                record.set(MEDICAL_RECORD_NUMBER_INDEX, record.get(MEDICAL_HISTORY_INDEX));

                // Moving contents from CHIEF_COMPLAINT to MEDICAL_HISTORY
                record.set(MEDICAL_HISTORY_INDEX, record.get(CHIEF_COMPLAINT_INDEX));

                // Moving contents from HISTORY_OF_PRESENT_ILLNESS to CHIEF_COMPLAINT
                record.set(CHIEF_COMPLAINT_INDEX, record.get(HISTORY_OF_PRESENT_ILLNESS_INDEX));

                // Moving contents from PHYSICAL_EXAMINATION to HISTORY_OF_PRESENT_ILLNESS
                record.set(HISTORY_OF_PRESENT_ILLNESS_INDEX, record.get(PHYSICAL_EXAMINATION_INDEX));

                // Moving contents from ASSESSMENT_AND_PLAN to PHYSICAL_EXAMINATION
                record.set(PHYSICAL_EXAMINATION_INDEX, record.get(ASSESSMENT_AND_PLAN_INDEX));

                // Moving contents from NHS_TRUST_REGION to ASSESSMENT_AND_PLAN
                record.set(ASSESSMENT_AND_PLAN_INDEX, record.get(NHS_TRUST_REGION_INDEX));

                // Moving contents from the last column without name to NHS_TRUST_REGION
                record.set(NHS_TRUST_REGION_INDEX, record.get(record.size()-1));

                // Removing the last column
                record.remove(record.size()-1);

                // Adding patient records to patientRecords
                patientRecords.add(record.toArray(new String[0]));

                // Classifying records by region
                String region = record.get(NHS_TRUST_REGION_INDEX);
                recordsByRegion.computeIfAbsent(region, k -> new ArrayList<>()).add(record.toArray(new String[0]));
            }
        }
    }

    // To parse CSV line considering quoted elements
    private List<String> parseCSVLine(String line) {
        List<String> elements = new ArrayList<>();
        // To get characters to form a complete value until a comma outside of quotes is encountered.
        StringBuilder sb = new StringBuilder();
        // To check if the character is inside quotes
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                elements.add(sb.toString().trim());
                sb.setLength(0); // To clear the StringBuilder for the next element
            } else {
                sb.append(c);
            }
        }

        // Adding the last element
        elements.add(sb.toString().trim());

        return elements;
    }

    // Searching patient records by NHS number
    public String[] searchByNHSNumber(String nhsNumber) {
        for (String[] record : patientRecords) {
            if (record[NHS_NUMBER_INDEX].equals(nhsNumber)) {
                return record;
            }
        }
        return null;
    }

    // Searching patient records by region
    public List<String[]> searchByRegion(String region) {
        List<String[]> matchedRecords = new ArrayList<>();
        for (String[] record : patientRecords) {
            // To check if the value at index record.length - 1 matches the specified region
// and add the record to matchedRecords if the condition is met.
            if (record.length > 0 && record[record.length - 1].equals(region)) {
                matchedRecords.add(record);
            }
        }
        return matchedRecords;
    }
    // Searching patient records by Medical History
    public List<String[]> searchByMedicalHistory(String medicalHistory) {
        List<String[]> matchedRecords = new ArrayList<>();

        // Splitting the medical history input into individual conditions
        String[] conditions = medicalHistory.split(",\\s*");

        for (String[] record : patientRecords) {
            // Getting the medical history string from the record
            String recordMedicalHistory = record[MEDICAL_HISTORY_INDEX];

            // Checking if the record's medical history contains all specified conditions
            boolean matchesAllConditions = true;
            for (String condition : conditions) {
                if (!recordMedicalHistory.contains(condition)) {
                    matchesAllConditions = false;
                    break;
                }
            }

            // Adding the record to matchedRecords if all conditions are matched
            if (matchesAllConditions) {
                matchedRecords.add(record);
            }
        }

        return matchedRecords;
    }



    // To interact with the user
    public static void main(String[] args) {
    classifier classifier = new classifier(); // To represent the classifier object
    Scanner scanner = new Scanner(System.in); // To initialize scanner to read user input
    System.out.println("Welcome to the Patient Record Classifier!"); // To Display welcome message

    try {
        // Loading patient records from HDFS input path
        classifier.loadPatientRecords("/mnt/c/Users/CC/Downloads/MedicalFiles.csv");
            while (true) {
                // To Display available options
                System.out.println("Choose your search criteria:");
                System.out.println("1. Search by NHS number");
                System.out.println("2. Search by region");
                System.out.println("3. Search by medical history");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                 // To consume newline
                scanner.nextLine(); 
                // Processing user choice based on the selected option.
                switch (choice) {
                    case 1:
                        System.out.print("Enter NHS number to search: ");
                        String nhsNumber = scanner.nextLine();
                        String[] patientRecord = classifier.searchByNHSNumber(nhsNumber);
                        // Displaying patient details if found, otherwise indicate patient not found.
                        if (patientRecord != null) {
                            // To print a message indicating patient was found
                            System.out.println("Patient found:");
                            // To print the patient's name present in the index of the NAME column
                            System.out.println("Name: " + patientRecord[NAME_INDEX]);
                            // To print the patient's age present in the index of the AGE column
                            System.out.println("Age: " + patientRecord[AGE_INDEX]);
                            // To print the patient's gender present in the index of the GENDER column
                            System.out.println("Gender: " + patientRecord[GENDER_INDEX]);
                            // To print the date of admission present in the index of the DATE_OF_ADMISSION column
                            System.out.println("Date of Admission: " + patientRecord[DATE_OF_ADMISSION_INDEX]);
                            // To print the medical record number present in the index of the MEDICAL_RECORD_NUMBER column
                            System.out.println("Medical Record Number: " + patientRecord[MEDICAL_RECORD_NUMBER_INDEX]);
                            // To print the patient's medical history present in the index of the MEDICAL_HISTORY column
                            System.out.println("Medical History: " + patientRecord[MEDICAL_HISTORY_INDEX]);
                            // To print the patient's chief complaint present in the index of the CHIEF_COMPLAINT column
                            System.out.println("Chief Complaint: " + patientRecord[CHIEF_COMPLAINT_INDEX]);
                            // To print the history of present illness present in the index of the HISTORY_OF_PRESENT_ILLNESS column
                            System.out.println("History of Present Illness: " + patientRecord[HISTORY_OF_PRESENT_ILLNESS_INDEX]);
                            // To print the physical examination details present in the index of the PHYSICAL_EXAMINATION column
                            System.out.println("Physical Examination: " + patientRecord[PHYSICAL_EXAMINATION_INDEX]);
                            // To print the assessment and plan present in the index of the ASSESSMENT_AND_PLAN column
                            System.out.println("Assessment and Plan: " + patientRecord[ASSESSMENT_AND_PLAN_INDEX]);
                            // To print the NHS trust region present in the index of the NHS_TRUST_REGION column
                            System.out.println("NHS Trust Region: " + patientRecord[NHS_TRUST_REGION_INDEX]);
                            System.out.println("~~~~~~~~~~~~~~");
                        } else {
                            System.out.println("Patient not found.");
                        }
                        break;
                    case 2:
                        System.out.print("Enter region name to search: ");
                        String region = scanner.nextLine();
                        // To retrieve patient records from the specified region.
                        List<String[]> recordsByRegion = classifier.searchByRegion(region);
                        // To display details of patients in the specified region.
                        System.out.println("Patients in region '" + region + "':");
                        for (String[] record : recordsByRegion) {
                            System.out.println("Name: " + record[NAME_INDEX]);
                            System.out.println("Age: " + record[AGE_INDEX]);
                            System.out.println("Gender: " + record[GENDER_INDEX]);
                            System.out.println("Date of Admission: " + record[DATE_OF_ADMISSION_INDEX]);
                            System.out.println("Medical Record Number: " + record[MEDICAL_RECORD_NUMBER_INDEX]);
                            System.out.println("Medical History: " + record[MEDICAL_HISTORY_INDEX]);
                            System.out.println("Chief Complaint: " + record[CHIEF_COMPLAINT_INDEX]);
                            System.out.println("History of Present Illness: " + record[HISTORY_OF_PRESENT_ILLNESS_INDEX]);
                            System.out.println("Physical Examination: " + record[PHYSICAL_EXAMINATION_INDEX]);
                            System.out.println("Assessment and Plan: " + record[ASSESSMENT_AND_PLAN_INDEX]);
                            System.out.println("NHS Trust Region: " + record[NHS_TRUST_REGION_INDEX]);
                            System.out.println("~~~~~~~~~~~~~~");
                        }
                        break;
                    case 3:
                        System.out.print("Enter medical history to search: ");
                        String medicalHistory = scanner.nextLine();
                        List<String[]> recordsByMedicalHistory = classifier.searchByMedicalHistory(medicalHistory);
                        for (String[] record : recordsByMedicalHistory) {
                            
                            System.out.println("Name: " + record[NAME_INDEX]);
                            System.out.println("Age: " + record[AGE_INDEX]);
                            System.out.println("Gender: " + record[GENDER_INDEX]);
                            System.out.println("Date of Admission: " + record[DATE_OF_ADMISSION_INDEX]);
                            System.out.println("Medical Record Number: " + record[MEDICAL_RECORD_NUMBER_INDEX]);
                            System.out.println("Medical History: " + record[MEDICAL_HISTORY_INDEX]);
                            System.out.println("Chief Complaint: " + record[CHIEF_COMPLAINT_INDEX]);
                            System.out.println("History of Present Illness: " + record[HISTORY_OF_PRESENT_ILLNESS_INDEX]);
                            System.out.println("Physical Examination: " + record[PHYSICAL_EXAMINATION_INDEX]);
                            System.out.println("Assessment and Plan: " + record[ASSESSMENT_AND_PLAN_INDEX]);
                            System.out.println("NHS Trust Region: " + record[NHS_TRUST_REGION_INDEX]);
                            System.out.println("~~~~~~~~~~~~~~");
                        }
                        break;

                    case 4:
                        System.out.println("Exiting...");  // To exit the program
                        System.exit(0);
                        // To handle invalid user input.
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
                }
            }
          // To handle any input/output exceptions
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

