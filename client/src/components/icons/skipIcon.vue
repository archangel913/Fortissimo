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
      <path
        d="M388.208,36.127v213.409c-0.37-0.318-0.588-0.79-1.025-1.04L13.252,25.244
		c-2.671-1.562-6.064-1.661-8.852-0.067C1.747,26.706,0,29.677,0,32.802v223.234v223.116c0,3.225,1.747,6.146,4.401,7.641
		c2.788,1.63,6.181,1.529,8.852-0.033l373.93-223.252c0.437-0.251,0.655-0.722,1.025-1.04v207.06H512V36.127H388.208z"
        style="fill: rgb(255, 255, 255)"
      ></path>
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
