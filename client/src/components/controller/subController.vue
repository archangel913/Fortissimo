<template>
  <div class="buttons">
    <MusicButton :button-type="'normal'" :icon-type="'shuffle'" :height="'50px'" :width="'50px'" @click="onShuffle" />
    <MusicButton :button-type="store.playingState.looping ? 'success' : 'failed'" :icon-type="'repeat'" :height="'50px'"
      :width="'50px'" @click="onLoop" />
  </div>
</template>

<script setup lang="ts">
import MusicButton from "@/components/musicButton.vue";

import { musicInformation } from '@/stores/musicInfomartion'
import { useWebsocket } from '@/websocket/connection'
import { channelId } from "@/main";

const store = musicInformation();
const websocket = useWebsocket();

const onShuffle = () => {
  store.isLoading = true;
  websocket.sendMessage('/app/player/' + channelId.value + '/shuffle', '');
}

const onLoop = () => {
  store.isLoading = true;
  if (store.playingState.looping) {
    websocket.sendMessage('/app/player/' + channelId.value + '/loopOff', '');
  } else {
    websocket.sendMessage('/app/player/' + channelId.value + '/loopOn', '');
  }
}

</script>

<style scoped>
.buttons {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}
</style>
