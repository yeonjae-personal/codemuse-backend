<script setup lang="ts">
import CfButton from "@/components/controls/CfButton.vue";
import CfFileInput from "@/components/controls/CfFileInput.vue";


import ArrowImg from "@/assets/arrow.png";
import { useGlobal } from "@/store";

const globalStore = useGlobal();
const jsonResult = ref<any>("");
const downloadLink = ref("");
const downloadLinkName = ref("");

const uploadFile = () => {
  document.getElementById("fileInput")?.click();
};

const convertFile = () => {
  const input = document.getElementById("fileInput");
  if (input && input.files && input.files[0]) {
    const file = input.files[0];

    // check file type
    const fileExtension = file.name.split(".").pop().toLowerCase();
    if (fileExtension !== "xlsx" && fileExtension !== "xls") {
      globalStore.setToastInfor(
        {
          title: "File Format Conversion",
          text: "Please upload a valid Excel file.",
          border: "start",
          borderColor: "white",
          type: "error",
          icon: "$error",
        },
        5000
      );
      return;
    }

    // read file
    const reader = new FileReader();
    reader.onload = (event: any) => {
      const data = event.target.result;
      const workbook = XLSX.read(data, { type: "binary" });
      const jsonData = XLSX.utils.sheet_to_json(
        workbook.Sheets[workbook.SheetNames[0]]
      );
      const isValidFormat = validateExcelFormat(jsonData);
      if (isValidFormat) {
        jsonResult.value = convertToJsonFormat(jsonData);
        const blob = new Blob([JSON.stringify(jsonResult.value, null, 2)], {
          type: "application/json",
        });
        const url = URL.createObjectURL(blob);
        downloadLink.value = url;

        //get file name
        downloadLinkName.value =
          file.name.slice(0, file.name.lastIndexOf(".")) + ".json";
      } else {
        downloadLinkName.value = "";
        jsonResult.value = "";
      }
    };
    reader.readAsBinaryString(file);
    globalStore.setToastInfor(
      {
        title: "File Format Conversion",
        text: "Convert the file successfully.",
        border: "start",
        borderColor: "white",
        type: "success",
        icon: "$success",
      },
      5000
    );
  } else {
    globalStore.setToastInfor(
      {
        title: "File Format Conversion",
        text: "Please upload an Excel file.",
        border: "start",
        borderColor: "white",
        type: "error",
        icon: "$error",
      },
      5000
    );
  }
};

const convertToJsonFormat = (jsonData: any) => {
  const formattedJson = {};
  jsonData.forEach((item: any) => {
    formattedJson[item.code] = item.text;
  });
  return formattedJson;
};

const validateExcelFormat = (jsonData: any) => {
  if (jsonData.length > 0) {
    // Check if the first object in the array has 'code' and 'text' properties
    const firstObject = jsonData[0];
    if (!("code" in firstObject) || !("text" in firstObject)) {
      globalStore.setToastInfor(
        {
          title: "File Format Conversion",
          text: "File format is not valid. 'code' and 'text' columns are required.",
          border: "start",
          borderColor: "white",
          type: "error",
          icon: "$error",
        },
        5000
      );
      return false;
    }
    // check if array has any value in 'code' and 'text' columns
    for (const row of jsonData) {
      if (!row.code || !row.text) {
        globalStore.setToastInfor(
          {
            title: "File Format Conversion",
            text: "File format is not valid. All rows should have values for 'code' and 'text'.",
            border: "start",
            borderColor: "white",
            type: "error",
            icon: "$error",
          },
          5000
        );
        return false;
      }
    }
    return true;
  }
  globalStore.setToastInfor(
    {
      title: "File Format Conversion",
      text: "File format is not valid. It should contain 'code' and 'text' columns in the first row.",
      border: "start",
      borderColor: "white",
      type: "error",
      icon: "$error",
    },
    5000
  );
  return false;
};

const downloadFile = () => {
  if (jsonResult.value) {
    const blob = new Blob([JSON.stringify(jsonResult.value, null, 2)], {
      type: "application/json",
    });

    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = downloadLinkName.value;
    document.body.appendChild(link);
    link.click();
    URL.revokeObjectURL(url);
    document.body.removeChild(link);
  } else {
    globalStore.setToastInfor(
      {
        title: "File Format Conversion",
        text: "No JSON data to download.",
        border: "start",
        borderColor: "white",
        type: "error",
        icon: "$error",
      },
      5000
    );
  }
};
</script>

<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <h1 class="mt-3 text-3xl font-extrabold tracking-tight text-slate-900">
      Message Management
    </h1>
    <div class="px-6">
      <div class="text-gray-500 mb-4">
        <span>
          User can upload an excel file with format as sample and convert to
          json file.
        </span>
        <br />
        <span>Please refer sample file as below</span>
      </div>
      <div class="flex items-center space-x-4 text-gray-500">
        <span>- Upload Excel Sample:</span>
       
      </div>
      <div class="flex items-center space-x-9 text-gray-500">
        <span>- Converted Sample:</span>
        <a
          href="/files/Message Management.json"
          download="Message Management.json"
          >Message Management.json</a
        >
      </div>
      <div class="flex items-center mt-2">
        <div class="m-8">
          <!-- Box 1 -->
          <div class="w-[300px] h-[200px] mb-5 text-truncate">
            <cf-file-input
              id="fileInput"
              class="custom-file-input"
              label="Drag and drop excel file here"
              prepend-icon=""
              variant="solo"
              accept=".xlsx, .xls"
            ></cf-file-input>
          </div>
          <div class="flex flex-row-reverse">
            <!-- Upload file -->
            <cf-button class="w-[110px]" label="Select" @click="uploadFile" />
          </div>
        </div>
        <div class="w-[200px] h-32 flex flex-col items-center">
          <img :src="ArrowImg" alt="arrow" class="m-0 my-3 w-[200px]" />
          <cf-button class="w-[110px]" label="Convert" @click="convertFile" />
        </div>
        <div class="m-8">
          <!-- Box 2-->
          <div
            class="border-2 border-gray-400 w-[300px] h-[200px] flex items-center justify-center mb-5 p-5 text-truncate"
          >
            <div v-if="downloadLinkName.length > 0" class="text-wrap">
              <a :href="downloadLink" :download="downloadLinkName">{{
                downloadLinkName
              }}</a>
            </div>
            <div v-else>
              <span class="text-gray-400 font-light">
                Json file will be displayed here
              </span>
            </div>
          </div>
          <div class="flex flex-row-reverse">
            <cf-button
              class="w-[110px]"
              label="Download"
              @click="downloadFile"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
a {
  @apply cursor-pointer text-blue-700 border-b-blue-800;
}

.custom-file-input :deep(.v-input__control .v-field) {
  box-shadow: none;
  border: 1px solid #9ca3af;
  border-radius: unset;
  max-width: 300px;
  height: 200px;
}

.custom-file-input :deep(.v-input__control .v-field .v-field__field) {
  display: flex;
  justify-content: center;
  align-items: center;
  text-wrap: wrap;
  padding: 5px;
}

.custom-file-input
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  display: flex;
  justify-content: center;
}
</style>
