import { ref } from 'vue'
import { defineStore } from 'pinia'

export interface QueueItem {
  title: string;
  url: string;
  id: number;
}

export interface NowPlayingItem {
  id: number;
  title: string;
  url: string;
  thumbnail: string;
}

export interface PlayingState {
  playing: boolean;
  looping: boolean;
}

export const musicInformation = defineStore('musicInformation', () => {
  const isLoading = ref<boolean>(true);
  const isStop = ref<boolean>(false);
  const queue = ref<QueueItem[]>([]);
  const nowPlayingItem = ref<NowPlayingItem>({
    id: 0,
    title: '',
    url: '',
    thumbnail: ''
  });
  const playingState = ref<PlayingState>({
    playing: false,
    looping: false
  });

  function updatePlaying(state:boolean){
    if(playingState.value){
      playingState.value.playing=state;
    }
  }

  function updateLooping(state:boolean){
    if(playingState.value){
      playingState.value.looping=state;
    }
  }

  function updatePlayingItem(id:number, title:string, url:string, thumbnail: Blob | null){
    if(nowPlayingItem.value){
      nowPlayingItem.value.id=id;
      nowPlayingItem.value.title=title;
      nowPlayingItem.value.url=url;
      if(nowPlayingItem.value.thumbnail){
        URL.revokeObjectURL(nowPlayingItem.value.thumbnail)
      }
      if(thumbnail){
        nowPlayingItem.value.thumbnail=URL.createObjectURL(thumbnail);
      } else {
        nowPlayingItem.value.thumbnail='';
      }
    }
  }

  function updateQueueItems(items:QueueItem[]){
    if(queue.value){
      queue.value=items;
    }
  }

  return { queue, nowPlayingItem, playingState, isLoading, isStop, updatePlaying, updateLooping, updatePlayingItem, updateQueueItems }
})
