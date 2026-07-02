<template>
  <div class="center border">
    <input v-model="url" @keyup.enter="postUrl" class="input" placeholder="http://example.com" />
    <MusicButton
      class="button"
      :button-type="'normal'"
      :icon-type="'plus'"
      :text="''"
      :height="'25px'"
      :width="'50px'"
      @click="postUrl"
    />
  </div>
</template>

<script setup lang="ts">
import MusicButton from "@/components/musicButton.vue";

import { ref } from 'vue';
import { useWebsocket } from '@/websocket/connection'
import { channelId } from "@/main";
import { musicInformation } from '@/stores/musicInfomartion.ts'

const websocket = useWebsocket();
const store = musicInformation();

const url = ref<string>('');

const postUrl = () => {
  store.isLoading = true;
  if(!url.value){
    console.error('URLを取得できませんでした。')
    store.isLoading = false;
    return;
  }

  websocket.sendMessage('/app/player/' + channelId.value + '/addMusic', url.value);
  url.value = '';
}
</script>

<style scoped>
.input {
  height: 25px;
  width: 70%;
  background-color: #232328;
  border: 1px solid #ffffff;
  border-radius: 5px;
  color: #ffffff;
}

.button {
  margin-left: 10px;
}

.center {
  display: flex;
  justify-content: center;
  align-items: center;
}

.border {
  border: 1px solid #000000;
  border-radius: 5px;
  box-sizing: border-box;
  background-color: #272722;
}
</style>
