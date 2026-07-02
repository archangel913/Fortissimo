<template>
  <svg
    version="1.1"
    id="_x32_"
    xmlns="http://www.w3.org/2000/svg"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    x="0px"
    y="0px"
    viewBox="0 0 349.03 349.031"
    style="opacity: 1"
    xml:space="preserve"
    :width="props.width"
    :height="props.height"
  >
    <g>
      <path
        d="M349.03,141.226v66.579c0,5.012-4.061,9.079-9.079,9.079H216.884v123.067c0,5.019-4.067,9.079-9.079,9.079h-66.579
		c-5.009,0-9.079-4.061-9.079-9.079V216.884H9.079c-5.016,0-9.079-4.067-9.079-9.079v-66.579c0-5.013,4.063-9.079,9.079-9.079
		h123.068V9.079c0-5.018,4.069-9.079,9.079-9.079h66.579c5.012,0,9.079,4.061,9.079,9.079v123.068h123.067
		C344.97,132.147,349.03,136.213,349.03,141.226z"
        style="fill: rgb(255, 255, 255)"
      />
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
