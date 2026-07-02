<template>
  <Transition name="fade" mode="out-in">
  <div class="thumbnail" :key="store.nowPlayingItem?.id">
    <div class="thumbnail-image">
      <img v-if="store.nowPlayingItem?.thumbnail" :src="store.nowPlayingItem?.thumbnail" alt="thumbnail" />
    </div>
    <div 
      ref="containerRef"
      class="hover-scroll" 
    >
      <span ref="textRef" class="hidden-detector">{{ store.nowPlayingItem?.title }}</span>
      <div
        v-if="isScrollActive"
        ref="scrollRef"
        class="marquee-inner"
        :style="{ '--speed-duration': duration + 's' }"
      >
        <a :href="store.nowPlayingItem?.url" class="marquee-item" 
          @click.prevent="openLink(store.nowPlayingItem?.url)">
          <span>{{ store.nowPlayingItem?.title }}</span>
        </a>
        <a :href="store.nowPlayingItem?.url" class="marquee-item"
          @click.prevent="openLink(store.nowPlayingItem?.url)">
          <span>{{ store.nowPlayingItem?.title }}</span>
        </a>
      </div>
      <div v-else>
         <a :href="store.nowPlayingItem?.url" class="non-scroll"
          @click.prevent="openLink(store.nowPlayingItem?.url)">
          {{ store.nowPlayingItem?.title }}
        </a>
      </div>
    </div>
  </div>
  </Transition>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, nextTick } from 'vue';
import { musicInformation } from '@/stores/musicInfomartion'
import { discordSdk } from "@/main";

const store = musicInformation();

const containerRef = ref<HTMLElement | null>(null);
const textRef = ref<HTMLElement | null>(null);
const scrollRef = ref<HTMLElement | null>(null);
const duration = ref(5);
const isScrollActive = ref(false);

const PIXELS_PER_SECOND = 120; 

const openLink = async (url: string) => {
  try{
    discordSdk.commands.openExternalLink({ url });
  } catch (error){
    console.error(error)
  }
}

const updateScrollStatus = async () => {
  await nextTick();
  if (!textRef.value || !containerRef.value) return;

  const containerWidth = containerRef.value.clientWidth;
  const pureTextWidth = textRef.value.offsetWidth;

  isScrollActive.value = pureTextWidth > containerWidth;

  if (isScrollActive.value) {
    await nextTick();
    if (scrollRef.value) {
      const totalWidth = scrollRef.value.scrollWidth;
      const moveDistance = totalWidth / 2;
      duration.value = moveDistance / PIXELS_PER_SECOND;
    }
  }
};

onMounted(updateScrollStatus);
watch(() => store.nowPlayingItem.title, updateScrollStatus, { flush: 'post' });
</script>

<style scoped>
.thumbnail {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-evenly;
  color: #ffffff;
}

.non-scroll{
  font-size: 24px;
  display: block;
  text-align: center;
}

.hover-scroll {
  width: 75%;
  overflow: hidden;
  white-space: nowrap;
  padding: 5px;
  cursor: pointer;
}

.marquee-inner {
  font-size: 24px;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
}

.marquee-item {
  padding-right: 100px;
}

.hover-scroll:hover .marquee-inner {
  display: flex;
  width: max-content;
  text-overflow: clip; 
  overflow: visible;
  animation: hover-marquee-loop var(--speed-duration, 5s) linear infinite;
}

a:link, a:visited {
  color: #ffffff;
  text-decoration: none;
}

.hidden-detector {
  white-space: nowrap; 
  font-size: 25px;
  position: absolute;
  visibility: hidden;
  pointer-events: none;
}

@keyframes hover-marquee-loop {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(-50%, 0);
  }
}

img {
  max-width: 100%;
  max-height: 100%;
  border: 1px solid #000000;
  border-radius: 5px;
  box-shadow: 1px 2px 2px #000000;
}

.thumbnail-image {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 80%;
  width: 80%;
}

.fade-enter-active,
.fade-leave-active {
  transition: all 1s ease;
}

.fade-enter-from{
  opacity: 0;
  transform: translateX(-100%);
}

.fade-leave-to {
  opacity: 0;
  transform: translateX(100%);
}
</style>
