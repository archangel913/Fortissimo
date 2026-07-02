<template>
  <div v-if="iconType == 'none'" class="music-button" :class="colorStyle" @click="handleClick">
    {{ props.text }}
  </div>
  <div v-else-if="iconType == 'pause'" class="music-button" :class="colorStyle" @click="handleClick">
    <PauseIcon :height="iconHeight" :width="iconWidth" />
  </div>
  <div v-else-if="iconType == 'skip'" class="music-button" :class="colorStyle" @click="handleClick">
    <SkipIcon :height="iconHeight" :width="iconWidth" />
  </div>
  <div v-else-if="iconType == 'plus'" class="music-button" :class="colorStyle" @click="handleClick">
    <PlusIcon :height="iconHeight" :width="iconWidth" />
  </div>
  <div v-else-if="iconType == 'play'" class="music-button" :class="colorStyle" @click="handleClick">
    <PlayIcon :height="iconHeight" :width="iconWidth" />
  </div>
  <div v-else-if="iconType == 'stop'" class="music-button" :class="colorStyle" @click="handleClick">
    <StopIcon :height="iconHeight" :width="iconWidth" />
  </div>
  <div
    v-else-if="iconType == 'repeat'"
    class="music-button"
    :class="colorStyle"
    @click="handleClick"
  >
    <RepeatIcon :height="iconHeight" :width="iconWidth" />
  </div>
  <div
    v-else-if="iconType == 'shuffle'"
    class="music-button"
    :class="colorStyle"
    @click="handleClick"
  >
    <ShuffleIcon :height="iconHeight" :width="iconWidth" />
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { convertSize } from "@/util/util";
import PauseIcon from "@/components/icons/pauseIcon.vue";
import ShuffleIcon from "@/components/icons/shuffleIcon.vue";
import SkipIcon from "@/components/icons/skipIcon.vue";
import RepeatIcon from "@/components/icons/repeatIcon.vue";
import PlusIcon from "@/components/icons/plusIcon.vue";
import PlayIcon from "@/components/icons/playIcon.vue";
import StopIcon from "@/components/icons/stopIcon.vue";

export type ButtonType = "normal" | "success" | "failed";
export type IconType =
  | "none"
  | "plus"
  | "play"
  | "skip"
  | "stop"
  | "pause"
  | "shuffle"
  | "repeat";

interface PropsType {
  buttonType: ButtonType;
  iconType?: IconType;
  text?: string;
  height: string;
  width: string;
}

const props = defineProps<PropsType>();

const emits = defineEmits(['click']);
const handleClick = () => {
  emits('click');
};

const colorStyle = computed(() => {
  switch (props.buttonType) {
    case "normal":
      return "normal-coler";
    case "success":
      return "success-coler";
    case "failed":
      return "failed-coler";
    default:
      return "";
  }
});

const iconType = computed(() => {
  switch (props.iconType) {
    case "play":
      return "play";
    case "plus":
      return "plus";
    case "pause":
      return "pause";
    case "skip":
      return "skip";
    case "stop":
      return "stop";
    case "shuffle":
      return "shuffle";
    case "repeat":
      return "repeat";
    default:
      return "none";
  }
});

const iconHeight = computed(() => {
  return convertSize(props.height, 0.75);
});

const iconWidth = computed(() => {
  return convertSize(props.width, 0.75);
});
</script>

<style scoped>
.music-button {
  user-select: none;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #000000;
  border-radius: 5px;
  color: #ffffff;
  box-shadow: 1px 2px 2px #000000;
  height: v-bind(height);
  width: v-bind(width);
}

.normal-coler {
  background-color: #5865f2;
}

.normal-coler:active {
  background-color: #474f98;
}

.success-coler {
  background-color: #248045;
}

.success-coler:active {
  background-color: #2d5d42;
}

.failed-coler {
  background-color: #da373c;
}

.failed-coler:active {
  background-color: #88383e;
}

.music-button:hover {
  cursor: pointer;
}

.music-button:active {
  box-shadow: inset 2px 2px #000000;
}
</style>
