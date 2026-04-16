<script setup lang="ts">
import { ref } from 'vue';
import { $t } from '@/locales';

defineOptions({
  name: 'Register'
});

const { toggleLoginModule } = useRouterPush();
const { formRef, validate } = useNaiveForm();
const agreementModalVisible = ref(false);
const privacyModalVisible = ref(false);

interface FormModel {
  username: string;
  password: string;
  confirmPassword: string;
}

const model: FormModel = reactive({
  username: '',
  password: '',
  confirmPassword: ''
});

const rules = computed<Record<keyof FormModel, App.Global.FormRule[]>>(() => {
  const { formRules, createConfirmPwdRule } = useFormRules();

  return {
    username: formRules.userName,
    password: formRules.pwd,
    confirmPassword: createConfirmPwdRule(model.password)
  };
});

const loading = ref(false);
async function handleSubmit() {
  await validate();
  loading.value = true;
  const { error } = await fetchRegister(model.username, model.password);
  if (!error) {
    window.$message?.success('注册成功');
    toggleLoginModule('pwd-login');
  }
  loading.value = false;
}

function openAgreementModal() {
  agreementModalVisible.value = true;
}

function openPrivacyModal() {
  privacyModalVisible.value = true;
}
</script>

<template>
  <div class="flex-col gap-0">
    <NForm ref="formRef" :model="model" :rules="rules" size="large" :show-label="false" @keyup.enter="handleSubmit">
      <NFormItem path="username">
        <NInput v-model:value="model.username" :placeholder="$t('page.login.common.userNamePlaceholder')">
          <template #prefix>
            <icon-ant-design:user-outlined />
          </template>
        </NInput>
      </NFormItem>
      <NFormItem path="password">
        <NInput
          v-model:value="model.password"
          type="password"
          show-password-on="click"
          :placeholder="$t('page.login.common.passwordPlaceholder')"
        >
          <template #prefix>
            <icon-ant-design:key-outlined />
          </template>
        </NInput>
      </NFormItem>
      <NFormItem path="confirmPassword">
        <NInput
          v-model:value="model.confirmPassword"
          type="password"
          show-password-on="click"
          :placeholder="$t('page.login.common.confirmPasswordPlaceholder')"
        >
          <template #prefix>
            <icon-ant-design:key-outlined />
          </template>
        </NInput>
      </NFormItem>
      <NSpace vertical :size="18" class="w-full">
        <NButton type="primary" size="large" round block :loading="loading" @click="handleSubmit">
          {{ $t('page.login.common.register') }}
        </NButton>
        <NButton block @click="toggleLoginModule('pwd-login')">
          {{ $t('page.login.common.back') }}
        </NButton>
      </NSpace>

      <div class="mt-4 text-center">
        注册即代表已阅读并同意我们的
        <NButton text type="primary" class="link-agreement" @click="openAgreementModal">用户协议</NButton>
        和
        <NButton text type="primary" class="link-privacy" @click="openPrivacyModal">隐私政策</NButton>
      </div>
    </NForm>

    <NModal v-model:show="agreementModalVisible" preset="card" title="用户协议" class="max-w-640px">
      <div class="flex-col gap-12px leading-7 text-14px color-#334155">
        <p>欢迎使用考辅智聊。在使用本系统前，请确认您已理解并同意以下内容。</p>
        <p>1. 本系统提供基于知识库检索与模型推理的学习辅助服务，生成内容仅供参考，不构成任何保证。</p>
        <p>2. 您应当妥善保管账号信息，不得以任何方式干扰、破坏平台的正常运行。</p>
        <p>3. 您上传、输入或生成的内容应符合法律法规及平台规范，不得包含违法或侵权信息。</p>
        <p>4. 平台可根据系统维护、功能调整或合规要求更新服务内容与协议条款。</p>
      </div>
    </NModal>

    <NModal v-model:show="privacyModalVisible" preset="card" title="隐私政策" class="max-w-640px">
      <div class="flex-col gap-12px leading-7 text-14px color-#334155">
        <p>我们重视您的个人信息与数据安全，并将按最小必要原则处理相关信息。</p>
        <p>1. 登录、对话、上传文件等数据仅用于身份校验、功能实现、系统优化与安全审计。</p>
        <p>2. 未经您的授权，我们不会将您的个人信息用于与本系统无关的用途。</p>
        <p>3. 对话内容、知识文件及操作记录可能用于故障排查、权限控制与服务质量改进。</p>
        <p>4. 如您对个人信息处理方式有疑问，可联系系统管理员进一步确认。</p>
      </div>
    </NModal>
  </div>
</template>

<style scoped></style>
