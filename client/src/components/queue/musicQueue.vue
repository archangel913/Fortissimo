<template>
  <TransitionGroup 
    name="queue-list" 
    tag="div"
    :class="['music-queue', { 'is-stop': store.isStop }]"
    @after-leave="afterLeave"
  >
      <MusicCard
        v-for="(item, index) in store.queue"
        :title="item.title"
        :key="item.id"
        :url="item.url"
      />
  </TransitionGroup>
</template>

<script setup lang="ts">
import MusicCard from "./musicCard.vue";
import { musicInformation } from '@/stores/musicInfomartion.ts'

const store = musicInformation();

const afterLeave = () => {
  store.isStop=false;
}
</script>

<style scoped>
.music-queue {
  position: relative;
  height: 100%;
  overflow-y: scroll;
  overflow-x: hidden;
  scrollbar-gutter: stable;
  scrollbar-color: rgba(255, 255, 255, 0.5) transparent;
  background-color: #202024;
  z-index: 10;
}

.music-queue .queue-list-move,
.queue-list-enter-active,
.queue-list-leave-active {
  transition: all 1s ease;
}

.queue-list-enter-from {
  opacity: 0;
  transform: translateX(-100%);
}

.music-queue .queue-list-leave-to {
  opacity: 0;
  transform: translateX(100%);
}

.music-queue .queue-list-leave-active {
  position: absolute;
  width: calc(100% - 30px);
}

.music-queue.is-stop .queue-list-leave-to {
  opacity: 0;
  transform: translateX(-100%);
}

.music-queue.is-stop .queue-list-leave-active {
  position: relative; 
  width: calc(100% - 30px);
}

</style>
