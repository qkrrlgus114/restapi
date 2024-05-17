<template>
  <div v-if="isVisible" class="modal-overlay">
    <div class="modal">
      <div class="modal-header">
        <slot name="header">Default Header</slot>
      </div>
      <div class="modal-body">
        <slot name="body">Default Body</slot>
      </div>
      <div class="modal-buttons">
        <button class="cancel" @click="onCancel">
          <slot name="cancel-btn">취소</slot>
        </button>
        <button class="confirm" @click="onConfirm">
          <slot name="confirm-btn">확인</slot>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";

const props = defineProps({
  isVisible: {
    type: Boolean,
    default: false,
  },
});

const emits = defineEmits(["confirm", "cancel"]);

const onConfirm = () => {
  emits("confirm");
};

const onCancel = () => {
  emits("cancel");
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: white;
  border-radius: 5px;
  padding: 30px 20px 10px 20px;
  width: 400px;
  height: 200px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.modal-header {
  font-size: 20px;
  font-weight: 1000;
  margin-bottom: 10px;
}

.modal-body {
  margin-bottom: 50px;
  text-align: center;
}

.modal-buttons {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.modal-buttons button {
  padding: 10px 60px;
  font-size: 16px;
  font-weight: 1000;
  cursor: pointer;
  border-radius: 4px;
  outline: none;
  border: none;
  transition: background-color 0.2s, border-color 0.2s;
}

.modal-buttons .cancel {
  background-color: #eeeeee;
  color: #5c5c5c;
}

.modal-buttons .cancel:hover {
  background-color: #dbdbdb;
  color: #5c5c5c;
}

.modal-buttons .confirm {
  background-color: #ffe8de;
  color: #f95421;
}

.modal-buttons .confirm:hover {
  background-color: #ffd5c3;
  color: #f95421;
}
</style>
