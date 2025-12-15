// ===============================
// 1) Read student info
// ===============================
const student = JSON.parse(localStorage.getItem("currentStudent"));

document.getElementById("studentName").innerText = student.name;
document.getElementById("studentId").innerText = student.id;
document.getElementById("studentLevel").innerText = student.level;


// ===============================
// 2) Load subjects saved by ADMIN
// ===============================
let levelsSubjects = JSON.parse(localStorage.getItem("levelsSubjects")) || {};

let subjects = levelsSubjects[student.level] || [];


// ===============================
// 3) Generate Assignments (4 per subject)
// ===============================
const assignBox = document.getElementById("assignList");
assignBox.innerHTML = "";

subjects.forEach(subject => {
    for (let i = 1; i <= 4; i++) {
        assignBox.innerHTML += `
            <div class="list-item">
                <p><strong>${subject} - Assignment ${i}</strong></p>
                <p>Due: Not Set</p>
                <a href="submit-assignment.html" class="start-btn">Submit</a>
            </div>
        `;
    }
});


// ===============================
// 4) Generate Quizzes (2 per subject)
// ===============================
const quizBox = document.getElementById("quizList");
quizBox.innerHTML = "";

subjects.forEach(subject => {
    for (let i = 1; i <= 2; i++) {
        quizBox.innerHTML += `
            <div class="list-item">
                <p><strong>${subject} - Quiz ${i}</strong></p>
                <p>Time: 60 minutes</p>
                <a href="take-quiz.html" class="start-btn">Start Quiz</a>
            </div>
        `;
    }
});
