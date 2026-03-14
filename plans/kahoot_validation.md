# Plan: AI Exam Generation with Validation and Failure Handling

This plan updates the AI exam generation process to include strict validation, retry logic, and logging of failed attempts.

## 1. Update Validation Logic in `DebugActivity.java`
- Limit AI fix attempts to **twice** (total of 3 generation attempts).
- In `validateAndProcessExam`:
    - If validation succeeds, save to the `exams` collection.
    - If validation fails and `retryCount < 2`, call `fixExamWithAI`.
    - If validation fails and `retryCount >= 2`, call `logFailedExam`.

## 2. Implement `logFailedExam`
- Create a new method to store invalid JSON data in a Firestore collection named `failed_exams`.
- Store the following fields:
    - `subject` (String)
    - `complexity` (String)
    - `invalidJson` (String)
    - `errorMessage` (String)
    - `createdBy` (String - UID)
    - `createdAt` (Timestamp)

## 3. Update `fixExamWithAI`
- Ensure the prompt explicitly mentions it's the last attempt if `retryCount == 1`.

## 4. Documentation
- Update this plan and ensure it reflects the logic accurately.
