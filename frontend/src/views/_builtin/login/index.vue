<script setup lang="ts">
import { computed } from 'vue';
import type { Component } from 'vue';
import { NConfigProvider } from 'naive-ui';
import { loginModuleRecord } from '@/constants/app';
import { useAppStore } from '@/store/modules/app';
import { useThemeStore } from '@/store/modules/theme';
import { $t } from '@/locales';
import loginIllustration from '@/assets/imgs/login-illustration.png';
import PwdLogin from './modules/pwd-login.vue';
import CodeLogin from './modules/code-login.vue';
import Register from './modules/register.vue';
import ResetPwd from './modules/reset-pwd.vue';
import BindWechat from './modules/bind-wechat.vue';

interface Props {
  /** The login module */
  module?: UnionKey.LoginModule;
}

const props = defineProps<Props>();

const appStore = useAppStore();
const themeStore = useThemeStore();

interface LoginModule {
  label: string;
  component: Component;
}

const moduleMap: Record<UnionKey.LoginModule, LoginModule> = {
  'pwd-login': { label: loginModuleRecord['pwd-login'], component: PwdLogin },
  'code-login': { label: loginModuleRecord['code-login'], component: CodeLogin },
  register: { label: loginModuleRecord.register, component: Register },
  'reset-pwd': { label: loginModuleRecord['reset-pwd'], component: ResetPwd },
  'bind-wechat': { label: loginModuleRecord['bind-wechat'], component: BindWechat }
};

const activeModule = computed(() => moduleMap[props.module || 'pwd-login']);

const pageStyle = computed(() => ({
  backgroundColor: '#c2c7d0',
  backgroundImage: `linear-gradient(120deg, rgba(226, 232, 240, 0.55) 0%, rgba(219, 226, 240, 0.35) 45%, rgba(213, 222, 238, 0.3) 100%), url(${loginIllustration})`
}));
</script>

<template>
  <NConfigProvider :theme="null" :theme-overrides="themeStore.naiveTheme">
  <div class="login-page login-force-light relative min-h-screen" :style="pageStyle">
    <div class="login-page-overlay"></div>
    <div class="login-layout relative z-4">
      <section class="login-hero lt-md:hidden">
        <div class="login-hero-top">
          <div class="flex-y-center gap-18px">
            <SystemLogo class="text-72px text-primary" />
            <div class="text-center">
              <h1 class="text-48px color-#0f172a font-700 leading-tight">{{ $t('system.title') }}</h1>
              <p class="mt-14px text-17px color-#0f172a leading-8">
                聊出未来高分！
              </p>
            </div>
          </div>
        </div>
      </section>

      <NCard :bordered="false" class="login-card relative">
        <div class="login-card-glow" aria-hidden="true"></div>
        <section class="login-form-panel relative">
          <header class="flex-y-center justify-between gap-16px">
            <div class="flex-y-center gap-12px">
              <SystemLogo class="text-52px text-primary md:hidden" />
              <div>
                <h3 class="login-title text-26px font-600 lt-sm:text-22px">{{ $t('system.title') }}</h3>
                <p class="mt-6px text-13px color-#64748b md:hidden">
                  基于 RAG 检索增强与 LLM 推理的考试复习聊天助手
                </p>
              </div>
            </div>
            <div class="i-flex-col shrink-0">
              <LangSwitch
                v-if="themeStore.header.multilingual.visible"
                :lang="appStore.locale"
                :lang-options="appStore.localeOptions"
                :show-tooltip="false"
                @change-lang="appStore.changeLocale"
              />
            </div>
          </header>
          <main class="pt-28px">
            <div class="flex-y-center gap-10px">
              <span class="login-accent-bar"></span>
              <h3 class="text-16px font-600 color-#0f172a tracking-wide">
                {{ $t(activeModule.label) }}
              </h3>
            </div>
            <div class="pt-22px">
              <Transition :name="themeStore.page.animateMode" mode="out-in" appear>
                <component :is="activeModule.component" />
              </Transition>
            </div>
          </main>
        </section>
      </NCard>
    </div>
  </div>
  </NConfigProvider>
</template>

<style scoped>
.login-page {
  position: relative;
  overflow: hidden;
  background-position: 4% center;
  background-repeat: no-repeat;
  background-size: cover;
}

.login-page-overlay {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 18% 24%, rgba(255, 255, 255, 0.42), transparent 31%),
    linear-gradient(90deg, rgba(15, 23, 42, 0.1) 0%, rgba(15, 23, 42, 0.06) 48%, rgba(15, 23, 42, 0.12) 100%);
  pointer-events: none;
}

.login-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 380px;
  min-height: 100vh;
  align-items: center;
  gap: 72px;
  padding: 48px 88px 48px 92px;
}

.login-card {
  width: 100%;
  border: 1.5px solid rgba(15, 23, 42, 0.78);
  border-radius: 28px;
  background:
    linear-gradient(160deg, rgba(255, 255, 255, 0.82) 0%, rgba(239, 246, 255, 0.62) 55%, rgba(224, 236, 255, 0.55) 100%);
  backdrop-filter: blur(22px) saturate(140%);
  -webkit-backdrop-filter: blur(22px) saturate(140%);
  box-shadow:
    0 0 0 1px rgba(255, 255, 255, 0.55) inset,
    0 0 0 4px rgba(255, 255, 255, 0.45),
    0 0 0 5px rgba(15, 23, 42, 0.3),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
  overflow: visible;
  transition: transform 0.4s ease, box-shadow 0.4s ease;
}

.login-card:hover {
  transform: translateY(-12px);
  box-shadow:
    0 0 0 1px rgba(255, 255, 255, 0.55) inset,
    0 0 0 4px rgba(255, 255, 255, 0.45),
    0 0 0 5px rgba(15, 23, 42, 0.3),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
}

.login-card::before {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: inherit;
  padding: 1px;
  background: linear-gradient(140deg, rgba(255, 255, 255, 0.9), rgba(147, 197, 253, 0.2) 40%, rgba(255, 255, 255, 0) 70%);
  -webkit-mask:
    linear-gradient(#000 0 0) content-box,
    linear-gradient(#000 0 0);
  -webkit-mask-composite: xor;
          mask-composite: exclude;
  pointer-events: none;
}


.login-card-glow {
  position: absolute;
  top: -40%;
  right: -30%;
  width: 320px;
  height: 320px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(96, 165, 250, 0.28), transparent 70%);
  filter: blur(10px);
  pointer-events: none;
}

.login-title {
  color: #1e3a8a;
  letter-spacing: 0.5px;
}

.login-accent-bar {
  display: inline-block;
  width: 4px;
  height: 16px;
  border-radius: 999px;
  background: linear-gradient(180deg, #3b82f6, #06b6d4);
  box-shadow: 0 0 12px rgba(59, 130, 246, 0.5);
}

.login-hero {
  min-height: 520px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.login-hero-top {
  position: relative;
  padding-left: 0;
  padding-top: 0;
  margin-top: -18vh;
  max-width: 520px;
}

.login-form-panel {
  padding: 36px 36px 32px;
  color: #0f172a;
}

.login-form-panel,
.login-form-panel :deep(span),
.login-form-panel :deep(.n-form-item-feedback) {
  color: #0f172a;
}

.login-form-panel :deep(.n-button--text) {
  --n-text-color: #1d4ed8 !important;
  --n-text-color-hover: #2563eb !important;
  --n-text-color-pressed: #1e40af !important;
  --n-text-color-focus: #2563eb !important;
}

.login-form-panel :deep(.link-agreement),
.login-form-panel :deep(.link-privacy) {
  --n-text-color: #2563eb !important;
  --n-text-color-hover: #1d4ed8 !important;
  --n-text-color-pressed: #1e40af !important;
  --n-text-color-focus: #1d4ed8 !important;
  font-weight: 700 !important;
  padding: 0 4px !important;
  text-shadow: 0 1px 0 rgba(255, 255, 255, 0.85), 0 2px 6px rgba(37, 99, 235, 0.35);
  border-bottom: none;
  border-radius: 0 !important;
  transition: transform 0.2s ease, text-shadow 0.2s ease;
}

.login-form-panel :deep(.link-agreement:hover),
.login-form-panel :deep(.link-privacy:hover) {
  transform: translateY(-1px);
  text-shadow: 0 1px 0 rgba(255, 255, 255, 0.9), 0 3px 10px rgba(37, 99, 235, 0.5);
}

.login-form-panel :deep(.n-input) {
  --n-border-radius: 14px !important;
  --n-color: rgba(255, 255, 255, 0.72) !important;
  --n-color-focus: rgba(255, 255, 255, 0.95) !important;
  --n-border: 1.5px solid rgba(30, 58, 138, 0.55) !important;
  --n-border-hover: 1.5px solid rgba(37, 99, 235, 0.85) !important;
  --n-border-focus: 1.5px solid rgba(37, 99, 235, 1) !important;
  --n-text-color: #0f172a !important;
  --n-placeholder-color: rgba(30, 41, 59, 0.75) !important;
  --n-box-shadow-focus: 0 0 0 3px rgba(59, 130, 246, 0.22) !important;
  backdrop-filter: blur(6px);
  -webkit-backdrop-filter: blur(6px);
  transition: box-shadow 0.25s ease, transform 0.25s ease;
}

.login-form-panel :deep(.n-input:hover) {
  box-shadow: 0 4px 14px -6px rgba(59, 130, 246, 0.28);
}

.login-form-panel :deep(.n-input .n-input__prefix) {
  color: #3b82f6;
  margin-right: 6px;
  font-size: 16px;
}

.login-form-panel :deep(.n-button) {
  --n-border-radius: 14px !important;
  font-weight: 500;
  letter-spacing: 0.3px;
}

.login-form-panel :deep(.n-form-item) {
  margin-bottom: 18px;
}

.login-form-panel :deep(.n-button--primary-type) {
  --n-color: linear-gradient(120deg, #2563eb 0%, #3b82f6 50%, #06b6d4 100%) !important;
  --n-color-hover: linear-gradient(120deg, #1d4ed8 0%, #2563eb 50%, #0891b2 100%) !important;
  --n-color-pressed: linear-gradient(120deg, #1e40af 0%, #1d4ed8 50%, #0e7490 100%) !important;
  --n-color-focus: linear-gradient(120deg, #1d4ed8 0%, #2563eb 50%, #0891b2 100%) !important;
  --n-text-color: #ffffff !important;
  --n-border: 1.5px solid rgba(30, 58, 138, 0.75) !important;
  --n-border-hover: 1.5px solid rgba(30, 58, 138, 0.95) !important;
  --n-border-focus: 1.5px solid rgba(30, 58, 138, 0.95) !important;
  --n-border-pressed: 1.5px solid rgba(30, 58, 138, 1) !important;
  box-shadow:
    0 10px 24px -10px rgba(37, 99, 235, 0.55),
    inset 0 1px 0 rgba(255, 255, 255, 0.35),
    inset 0 0 0 1px rgba(255, 255, 255, 0.22);
  transition: transform 0.2s ease, box-shadow 0.3s ease;
}

.login-form-panel :deep(.n-button--primary-type:hover) {
  transform: translateY(-1px);
  box-shadow: 0 14px 30px -10px rgba(37, 99, 235, 0.6), inset 0 1px 0 rgba(255, 255, 255, 0.35);
}

.login-form-panel :deep(.n-button--primary-type:active) {
  transform: translateY(0);
}

.login-form-panel :deep(.n-button:not(.n-button--primary-type)) {
  --n-color: rgba(255, 255, 255, 0.7) !important;
  --n-color-hover: rgba(224, 236, 255, 0.85) !important;
  --n-color-pressed: rgba(191, 219, 254, 0.8) !important;
  --n-color-focus: rgba(224, 236, 255, 0.85) !important;
  --n-text-color: #1e40af !important;
  --n-text-color-hover: #1d4ed8 !important;
  --n-border: 1.5px solid rgba(30, 58, 138, 0.55) !important;
  --n-border-hover: 1.5px solid rgba(37, 99, 235, 0.85) !important;
  --n-border-focus: 1.5px solid rgba(37, 99, 235, 1) !important;
  --n-border-pressed: 1.5px solid rgba(30, 58, 138, 1) !important;
  backdrop-filter: blur(6px);
  -webkit-backdrop-filter: blur(6px);
}

.login-form-panel :deep(.n-divider) {
  --n-color: rgba(148, 184, 255, 0.4) !important;
}

.login-form-panel :deep(.n-divider__title) {
  color: #64748b !important;
  font-size: 12px;
}



@media (max-width: 1024px) {
  .login-layout {
    grid-template-columns: minmax(0, 1fr) 360px;
    gap: 36px;
    padding: 32px 36px;
  }

  .login-page {
    background-position: 8% center;
  }

  .login-hero-top {
    margin-top: -10vh;
    max-width: 460px;
  }
}

@media (max-width: 768px) {
  .login-page {
    background-position: center top;
    background-size: cover;
  }

  .login-page-overlay {
    background:
      linear-gradient(180deg, rgba(255, 255, 255, 0.74), rgba(255, 255, 255, 0.48));
  }

  .login-layout {
    grid-template-columns: 1fr;
    justify-items: center;
    padding: 20px 16px;
  }

  .login-card {
    width: min(100%, 420px);
  }

  .login-form-panel {
    padding: 28px 22px;
  }
}
</style>
