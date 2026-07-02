<template>
  <div v-if="store.isLoading" class="loading-overlay">
    <svg class="circle-spinner" viewBox="0 0 50 50">
      <circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle>
    </svg>
    <b>読み込み中</b>
  </div>
</template>

<script setup lang="ts">
import { musicInformation } from '@/stores/musicInfomartion.ts'

const store = musicInformation();
</script>

<style scoped>
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  color: #ffffff;
}

.circle-spinner {
  animation: spin 2s linear infinite;
  width: 50px;
  height: 50px;
}

.circle-spinner .path {
  stroke: #5865f2;
  stroke-linecap: round; 
  stroke-dasharray: 1, 150;
  stroke-dashoffset: 0;
  animation: dash 1.5s ease-in-out infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@keyframes dash {
  0% {
    stroke-dasharray: 1, 150;
    stroke-dashoffset: 0;
  }
  50% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -35;
  }
  100% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -124;
  }
}
</style>
