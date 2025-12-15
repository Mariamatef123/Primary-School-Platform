package com.first.first_app.DTO;

import jakarta.validation.constraints.NotBlank;

public  class PhoneDTO {
        @NotBlank(message = "Phone number is required")
        private String phoneNumber;

        @NotBlank(message = "Phone type is required")
        private String phoneType; // e.g., MOBILE, HOME, WORK

        public PhoneDTO() {}

        public PhoneDTO(String phoneNumber, String phoneType) {
            this.phoneNumber = phoneNumber;
            this.phoneType = phoneType;
        }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

        public String getPhoneType() { return phoneType; }
        public void setPhoneType(String phoneType) { this.phoneType = phoneType; }
    }