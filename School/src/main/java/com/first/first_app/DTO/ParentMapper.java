// package com.first.first_app.DTO;

// import com.first.first_app.Model.Parent;
// import com.first.first_app.Model.Student;
// import com.first.first_app.Model.UserPhone;

// import java.util.List;
// import java.util.stream.Collectors;

// public class ParentMapper {

//     // -----------------------------
//     // Convert Parent entity → ParentDTO
//     // -----------------------------
//     public static ParentDTO toDTO(Parent parent) {
//         ParentDTO dto = new ParentDTO();
//         dto.setId(parent.getId());
//         dto.setName(parent.getName());
//         dto.setEmail(parent.getEmail());
//         dto.setPassword(parent.getPassword());

//         // Convert children → ChildDTO
//         if (parent.getChildren() != null) {
//             dto.setChildren(
//                 parent.getChildren().stream()
//                     .map(toChildDTO)
//                     .collect(Collectors.toList())
//             );
//         }

//         // Convert phones → PhoneDTO
//         if (parent.getPhones() != null) {
//             dto.setPhones(
//                 parent.getPhones().stream()
//                     .map(ParentMapper::toPhoneDTO)
//                     .collect(Collectors.toList())
//             );
//         }

//         return dto;
//     }

//     // -----------------------------
//     // Convert ParentDTO → Parent entity
//     // -----------------------------
//     public static Parent toEntity(ParentDTO dto) {
//         Parent parent = new Parent();
//         parent.setId(dto.getId());
//         parent.setName(dto.getName());
//         parent.setEmail(dto.getEmail());
//         parent.setPassword(dto.getPassword());

//         // Convert children DTO → Student entities
//         if (dto.getChildren() != null) {
//             List<Student> children = dto.getChildren().stream()
//                 .map(ParentMapper::toStudentEntity)
//                 .collect(Collectors.toList());
//             parent.setChildren(children);
//         }

//         // Convert phone DTO → UserPhone entities
//         if (dto.getPhones() != null) {
//             List<UserPhone> phones = dto.getPhones().stream()
//                 .map(ParentMapper::toPhoneEntity)
//                 .collect(Collectors.toList());
//             parent.setPhones(phones);
//         }

//         return parent;
//     }

//     // -----------------------------
//     // Convert Child entity → DTO
//     // -----------------------------
//     public static ParentDTO.Child toChildDTO(Student child) {
//         ParentDTO.Child dto = new ParentDTO.Child();
//         dto.setId(child.getId());
//         dto.setName(child.getName());
//         dto.setAge(child.getAge());
//         return dto;
//     }

//     // -----------------------------
//     // Convert Child DTO → Student entity
//     // -----------------------------
//     public static Student toStudentEntity(ParentDTO.Child dto) {
//         Student student = new Student();
//         student.setId(dto.getId());
//         student.setName(dto.getName());
//         student.setAge(dto.getAge());
//         return student;
//     }

//     // -----------------------------
//     // Convert UserPhone entity → PhoneDTO
//     // -----------------------------
//     public static ParentDTO.Phone toPhoneDTO(UserPhone phone) {
//         return new ParentDTO.Phone(phone.getPhoneNumber(), phone.getPhoneType());
//     }

//     // -----------------------------
//     // Convert PhoneDTO → UserPhone entity
//     // -----------------------------
//     public static UserPhone toPhoneEntity(ParentDTO.Phone dto) {
//         return new UserPhone(dto.getPhoneNumber(), dto.getPhoneType());
//     }
// }
