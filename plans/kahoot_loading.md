# Plan: Load AI-Generated Kahoot Games from Firestore

This plan outlines the steps to allow users to select and play AI-generated exams stored in the Firebase `exams` collection.

## 1. Modify `ExamActivity.java`
- Update `onCreate` to check for a `documentId` in the `Intent` extras.
- If a `documentId` is present:
    - Fetch the document from Firestore collection `exams`.
    - Retrieve the `content` field (JSON string).
    - Parse the JSON and initialize the exam.
- If no `documentId` is present, fall back to loading `exam.json` from assets.

## 2. Create `ExamListActivity.java` & `activity_exam_list.xml`
- This activity will display a list of all available exams from Firestore.
- Each item will show the `subject`, `createdBy`, and `createdAt` fields.
- Clicking an item will start `ExamActivity` passing the selected `documentId`.
- Show an alert if the collection is empty.

## 3. Update `DebugActivity.java`
- Add a new button `btnLoadKahoot` with the text "Load AI Kahoot Game".
- Implement a click listener that starts `ExamListActivity`.

## 4. Update `AndroidManifest.xml`
- Register the new `ExamListActivity`.

## 5. UI Improvements
- Use a `RecyclerView` in `ExamListActivity` for a smooth, detailed list.
- Show a progress bar while fetching exams from Firestore.
