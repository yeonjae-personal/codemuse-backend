<template>
  <div>
    <v-container>
      <v-row>
        <v-col cols="12">
          <v-text-field
            v-model="searchTerm"
            label="Search"
            outlined
            dense
            @input="emitSearchTerm"
          ></v-text-field>
        </v-col>
      </v-row>
    </v-container>

    <v-container>
      <v-row>
        <v-col cols="12">
          <v-data-table
            :headers="headers"
            :items="items"
            :search="searchTerm"
          ></v-data-table>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {
  emits: ["update-search-term"],
  data() {
    return {
      searchTerm: "",
      headers: [
        { text: "Name", value: "name" },
        { text: "Email", value: "email" },
      ],
      items: [
        { name: "John Doe", email: "john@example.com" },
        { name: "Jane Smith", email: "jane@example.com" },
      ],
    };
  },
  methods: {
    emitSearchTerm() {
      this.$emit("update-search-term", this.searchTerm);
    },
  },
};
</script>
