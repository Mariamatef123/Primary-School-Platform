package com.first.first_app.Repo;

import org.springframework.stereotype.Repository;

import com.first.first_app.Model.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

User existsByEmail(String email);

User findByEmail(String email);



//     private ArrayList<User> users = new ArrayList<>();
//     public UserRepo() {
//         users.add(new User("Alice",1, 30, "mariam@gmail.com","1234"));
//         users.add(new User("Bob",2,25, "Malak@gmail.com","5678"));
//     }
// public boolean check(String name, String password) {
//     for (User u : users) {
//         if (u.getName().equals(name) && u.getPassword().equals(password)) {
//             return true;
//         }
//     }
//     return false;
// }

//     public String saveUser(User user) {
//         for (User u : users) {
//             if (u.getName().equalsIgnoreCase(user.getName())) {
//                 return "name already exists!";
//             }
//         }
//         users.add(user);
//         return "register successfully";
//     }
//     public ArrayList<User> findAll() {
//         return users;
//     }
//     public void updateUser(User user) {
//         for (int i = 0; i < users.size(); i++) {
//             if (users.get(i).getId()==(user.getId())) {
//                 users.set(i, user);
//                 return;
//             }
//         }
//     }
//     public String deleteUser(int id) {
//         for (int i = 0; i < users.size(); i++) {
//             if (users.get(i).getId()==(id)) {
//                 users.remove(i);
//                 return "Deleted successfully";
//             }
//         }
//         return "User not found";
//     }
}
