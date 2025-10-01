<template>
  <v-navigation-drawer>
    <v-list nav dense>
      <v-list-item
        v-for="item in items"
        :key="item.title"
        :title="item.title"
        :prepend-icon="item.icon"
        :value="item.active"
        :active="item.active"
        @click="toggleActive(item)"
      >
        <template #append>
          <v-icon v-if="item.items">
            {{ item.active ? "mdi-menu-down" : "mdi-menu-right" }}
          </v-icon>
        </template>

        <v-list-group v-if="item.items" :value="item.active">
          <v-list-item
            v-for="subItem in item.items"
            :key="subItem.title"
            :title="subItem.title"
            :value="subItem.active"
            @click="
              toggleActive(subItem);
              showContent(subItem);
            "
          ></v-list-item>
        </v-list-group>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
  <v-main max-width="500" justify="center">
    <component
      :is="currentComponent"
      @update-search-term="searchTerm = $event"
    ></component>
  </v-main>
</template>

<script>
import SearchGrid from "./SearchGridPage.vue";
import InputGrid from "./InputGridPage.vue";

export default {
  components: {
    SearchGrid,
    InputGrid,
  },
  data() {
    return {
      items: [
        { title: "Dashboard", icon: "mdi-view-dashboard" },
        {
          title: "SamplePage",
          // icon: 'mdi-folder',
          active: true,
          items: [
            { title: "1.1 retrieveEmployee", active: false },
            { title: "1.2 createEmployee", active: false },
            { title: "1.3", active: false },
          ],
        },
        {
          title: "Menu 2",
          // icon: 'mdi-folder',
          active: false,
          items: [
            { title: "Submenu 2.1", active: false },
            { title: "Submenu 2.2", active: false },
          ],
        },
      ],
      currentComponent: null,
      searchTerm: "",
    };
  },
  methods: {
    toggleActive(item) {
      this.items.forEach((i) => {
        if (i !== item) {
          i.active = false;
          if (i.items) {
            i.items.forEach((subItem) => (subItem.active = false));
          }
        }
      });
      item.active = !item.active;
    },
    showContent(subItem) {
      if (subItem.title === "1.1 retrieveEmployee") {
        this.currentComponent = SearchGrid;
      } else if (subItem.title === "1.2 createEmployee") {
        this.currentComponent = InputGrid;
      } else {
        this.currentComponent = null;
      }
    },
  },
};
</script>
