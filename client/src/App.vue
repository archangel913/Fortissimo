<template>
  <LoadingOverlay />
  <MusicPlayer />
</template>

<script setup lang="ts">
import MusicPlayer from "@/views/musicPlayer.vue";

import { onMounted } from 'vue'
import { useWebsocket } from '@/websocket/connection'
import { musicInformation } from '@/stores/musicInfomartion'

import { discordSdk, channelId, guildId } from "@/main";
import { base64ToBlob } from "@/util/util"
import LoadingOverlay from "@/components/loadingOverlay.vue";

const store = musicInformation();
const websocket = useWebsocket();

const onRecieve = (data: any) => {
  store.updatePlayingItem(data.now.id, data.now.title, data.now.url, base64ToBlob(data.now.thumbnail))

  data.queue.sort(function(a: any,b: any){
    if(a.order<b.order) return -1;
    if(a.order > b.order) return 1;
    return 0;
  });
  store.updateQueueItems(data.queue);
  store.updatePlaying(data.status.playing);
  store.updateLooping(data.status.looping);
  store.isLoading = false;
}

onMounted(async () => {
  await discordSdk.ready();
  console.log("Discord SDK is ready");

  if(!guildId){
    console.error('初期化に必要な情報が足りません')
    return;
  }

  const { code } = await discordSdk.commands.authorize({
    client_id: import.meta.env.VITE_DISCORD_CLIENT_ID,
    response_type: 'code',
    state: '',
    prompt: 'none',
    scope: ['identify', 'guilds'],
  });

  const response = await fetch('.proxy/ff/api/auth', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ 'code': code, 'guildId': guildId.value })
  });

  const token = await response.text();

  await websocket.connect('.proxy/ff/connect', '/topic/'+channelId.value+'/allInformation', token, onRecieve);
  websocket.sendMessage('/app/player/'+channelId.value+'/getAllInformation', '');
})
</script>

<style>
html,
body {
  margin: 0;
  padding: 0;
  background-color: #202024;
  overflow: hidden !important;
}
</style>
