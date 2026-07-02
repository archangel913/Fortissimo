<template>
  <svg
    version="1.1"
    id="_x32_"
    xmlns="http://www.w3.org/2000/svg"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    x="0px"
    y="0px"
    viewBox="0 0 512 512"
    style="opacity: 1"
    xml:space="preserve"
    :width="props.width"
    :height="props.height"
  >
    <g>
      <rect
        x="21"
        y="21"
        width="470"
        height="470"
        style="fill: rgb(255, 255, 255)"
      ></rect>
    </g>
  </svg>
</template>

<script setup lang="ts">
interface PropsType {
  height: string;
  width: string;
}

const props = defineProps<PropsType>();
</script>
