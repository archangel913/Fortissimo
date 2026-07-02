<template>
  <div class="display-flex">
    <div class="herf">
      <MusicQueue class="queue-height" />
      <UrlFrom class="input-height" />
    </div>
    <div class="herf">
      <MusicController class="controller-height" />
    </div>
  </div>
</template>

<script setup lang="ts">
import MusicController from "@/components/controller/musicController.vue";
import MusicQueue from "@/components/queue/musicQueue.vue";
import UrlFrom from "@/components/urlFrom.vue";
</script>

<style scoped>
.display-flex {
  display: flex;
}

.herf {
  position: relative;
  width: 50%;
  height: 100vh;
}

.queue-height {
  height: 90vh;
}

.input-height {
  height: 10vh;
}

.controller-height {
  height: 100vh;
}
</style>
