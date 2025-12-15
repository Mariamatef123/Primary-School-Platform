// change nav styles on scroll

window.addEventListener('scroll',() =>{
    document.querySelector('nav').classList.toggle('window-scroll', window.scrollY >0)})






//Login 

const users = [
    { id: "s101", email: "student1@mail.com", password: "123456", role: "student", name: "Student One" },
    { id: "t201", email: "teacher1@mail.com", password: "123456", role: "teacher", name: "Teacher One" },
    { id: "p301", email: "parent1@mail.com", password: "123456", role: "parent", name: "Parent One" },
    { id: "a001", email: "admin@mail.com", password: "123456", role: "admin", name: "Admin" }
];

// Elements
const form = document.getElementById("form");
const errorMessage = document.getElementById("error-message");

form.addEventListener("submit", function(e){
    e.preventDefault();

    // Read inputs
    const userId = document.getElementById("userId").value.trim();
    const password = document.getElementById("password").value.trim();
    const role = document.getElementById("role").value.trim();

    // Empty validation
    if(userId === "" || password === "" || role === ""){
        showError("Please fill all fields");
        return;
    }

    // Find user 
    const loggedUser = users.find(user => 
        (user.email === userId || user.id === userId) &&
        user.password === password &&
        user.role === role
    );

    if(!loggedUser){
        showError("Invalid login information");
        return;
    }

    // Save to localStorage
    localStorage.setItem("currentUser", JSON.stringify(loggedUser));

    // Redirect based on role
    switch(loggedUser.role){
        case "student":
            window.location.href = "dashboard-student.html";
            break;
        case "teacher":
            window.location.href = "dashboard-teacher.html";
            break;
        case "parent":
            window.location.href = "dashboard-parent.html";
            break;
        case "admin":
            window.location.href = "dashboard-admin.html";
            break;
    }
});

// show error
function showError(msg){
    errorMessage.innerText = msg;
    errorMessage.style.opacity = 1;


    setTimeout(() => {
        errorMessage.style.opacity = 0;
    }, 3000);
}
