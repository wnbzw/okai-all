<template>
  <div id="userProfilePage">
    <h2 style="margin-bottom: 16px">个人资料</h2>
    <div style="display: flex; align-items: center; margin-bottom: 16px">
      <img
        class="avatar"
        :src="loginUserStore.loginUser.userAvatar"
        alt="用户头像"
        style="width: 100px; height: 100px; border-radius: 50%"
      />
      <div style="margin-left: 16px">
        <h3>{{ loginUserStore.loginUser.userName }}</h3>
        <p>账户: {{ loginUserStore.loginUser.userAccount }}</p>
        <p>简介: {{ loginUserStore.loginUser.userProfile ?? "暂无简介" }}</p>
      </div>
    </div>
    <a-button type="primary" @click="editProfile">编辑资料</a-button>
    <a-button type="danger" @click="logout">退出登录</a-button>
  </div>
</template>

<script setup lang="ts">
import { useLoginUserStore } from "@/store/userStore";
import { useRouter } from "vue-router";
import { userLogoutUsingPost } from "@/api/userController";

const loginUserStore = useLoginUserStore();
const router = useRouter();

/**
 * 编辑资料
 */
const editProfile = () => {
  router.push({
    path: "/user/edit-profile",
  });
};

/**
 * 退出登录
 */
const logout = async () => {
  const res = await userLogoutUsingPost();
  if (res) {
    loginUserStore.logout();
    router.push({
      path: "/user/login",
    });
  }
};
</script>

<style scoped>
#userProfilePage {
  padding: 24px;
}

.avatar {
  border-radius: 50%;
}

h3 {
  margin: 0 0 8px;
}

p {
  margin: 4px 0;
}
</style>
