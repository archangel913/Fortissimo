<template>
  <div class="controller">
    <ThumbnailViewer class="thumbnail-viewer"/>
    <MainController class="main-controller" />
    <SubController class="sub-controller" />
  </div>
</template>

<script setup lang="ts">
import MainController from "@/components/controller/mainController.vue";
import ThumbnailViewer from "@/components/controller/thumbnailViewer.vue";
import SubController from "@/components/controller/subController.vue";
</script>

<style scoped>
.controller {
  border: 1px solid #000000;
  border-radius: 5px;
  box-sizing: border-box;
  height: 100%;
  background-color: #494949;
}

.thumbnail-viewer {
  height: 70%;
}

.main-controller {
  height: 20%;
}

.sub-controller {
  height: 10%;
}
</style>
