<template>
  <v-container fluid>
    <v-row max-width="600">
      <v-col cols="12">
        <v-text-field
          v-model="formData.name"
          label="Name"
          outlined
          dense
        ></v-text-field>
      </v-col>
      <v-col cols="12">
        <v-text-field
          v-model="formData.email"
          label="Email"
          outlined
          dense
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row max-width="600">
      <v-col cols="12">
        <v-btn color="primary" @click="submitForm">Submit</v-btn>
        <v-btn color="error" @click="resetForm">Reset</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        name: "",
        email: "",
      },
    };
  },
  methods: {
    submitForm() {
      //
    },
    resetForm() {
      fetch("http://localhost:8080/ordr/employee/retrieveEmployee/10004")
        .then((response) => response.json())
        .then((data) => {
          this.apiData = data;
        })
        .catch((error) => {
          console.error("Error fetching data:", error);
        });
    },
  },
};
</script>
