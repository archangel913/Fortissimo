<template>
  <div>
    <div class="seek-bar"></div>
    <div class="buttons">
      <MusicButton :button-type="'normal'" :icon-type="'stop'" :height="'50px'" :width="'50px'" @click="onStop" />
      <MusicButton v-if="store.playingState.playing" :button-type="'normal'" :icon-type="'pause'" :height="'50px'"
        :width="'50px'" @click="onPause" />
      <MusicButton v-else :button-type="'normal'" :icon-type="'play'" :height="'50px'" :width="'50px'"
        @click="onResume" />
      <MusicButton :button-type="'normal'" :icon-type="'skip'" :height="'50px'" :width="'50px'" @click="onSkip" />
    </div>
  </div>
</template>

<script setup lang="ts">
import MusicButton from "@/components/musicButton.vue";

import { musicInformation } from '@/stores/musicInfomartion'
import { useWebsocket } from '@/websocket/connection'

const store = musicInformation();
const websocket = useWebsocket();
import { channelId } from "@/main";

const onPause = () => {
  store.isLoading = true;
  websocket.sendMessage('/app/player/' + channelId.value + '/pause', '');
}

const onResume = () => {
  store.isLoading = true;
  websocket.sendMessage('/app/player/' + channelId.value + '/resume', '');
}

const onStop = () => {
  store.isStop = true;
  store.isLoading = true;
  websocket.sendMessage('/app/player/' + channelId.value + '/stop', '');
}

const onSkip = () => {
  store.isLoading = true;
  websocket.sendMessage('/app/player/' + channelId.value + '/skip', '');
}

</script>

<style scoped>
.seek-bar {
  height: 50%;
}

.buttons {
  height: 50%;
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}
</style>
