<template>
  <div class="modal-overlay" v-if="isVisible">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title"><slot name="header"></slot></h5>
        </div>
        <div class="modal-body">
          <slot name="body"></slot>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn cancel-btn" @click="onCancel">
            <slot name="cancel-btn">취소</slot>
          </button>
          <button type="button" class="btn confirm-btn" @click="onConfirm">
            <slot name="confirm-btn">적용</slot>
          </button>
        </div>
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

.modal-content {
  background: white;
  border-radius: 5px;
  padding: 50px 30px 20px 30px;
  width: 400px;
}

.modal-title {
  font-weight: 1000;
}

.modal-body {
  padding: 20px 0px;
}

.modal-footer button:nth-child(2) {
  margin-left: 20px;
}

.cancel-btn {
  background-color: #eeeeee;
  color: #5c5c5c;
}

.cancel-btn,
.confirm-btn {
  padding: 10px 20px;
  font-size: 16px;
  font-weight: 1000;
  cursor: pointer;
  border-radius: 4px;
  outline: none;
  border: none;
  transition: background-color 0.2s, border-color 0.2s;
}

.cancel-btn:hover {
  background-color: #c7c7c7;
  border-color: #c7c7c7;
}

.cancel-btn:active {
  background-color: #c7c7c7;
  border-color: #c7c7c7;
}

.confirm-btn:hover {
  background-color: #ffcdb8;
  border-color: #ffcdb8;
}
.confirm-btn {
  background-color: #ffe8de;
  color: #db0700;
}
</style>
