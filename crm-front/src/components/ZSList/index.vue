<!-- 全局列表界面布局组件 -->
<template>
  <div
    v-loading="loading"
    class="main-list"
    :class="{ 'full-screen': isFullScreen }"
    :style="isFull ? { 'left': '0', 'top': '0' } : userStore.isCollapse ? {
      'left': '64px',
      'transition': '.3s'
    } : {}"
  >
    <!-- 列表头部 -->
    <div v-if="isHead" class="list-head">
      <!-- 列表头部的左侧内容 · 标题展示 · 可使用插槽自定义内容 -->
      <div class="list-head__lt">
        <slot name="head-left">
          <div class="custom-title">{{ title }}</div>
        </slot>
      </div>
      <!-- 列表头部的右侧内容 -->
      <div class="list-head__rt">
        <!-- 插槽自定义内容 -->
        <slot name="head-right" />
        <!-- 收起搜索部分按钮 -->
        <el-tooltip effect="dark" :content="`${ bodySearchShow ? '收起' : '展开' }搜索条件`" placement="top">
          <el-icon
            class="list-head__rt-icon"
            v-if="isBodySearch && isFold"
            @click="bodySearchShow = !bodySearchShow"
            :size="20"
          >
            <ArrowDown v-if="bodySearchShow" />
            <ArrowUp v-else />
          </el-icon>
        </el-tooltip>
      </div>
    </div>
    <!-- 列表身体 -->
    <div class="list-body">
      <!-- 身体左侧内容 -->
      <div v-if="isBodyLeft" class="list-body__lt">
        <slot name="body-left" />
      </div>
      <!-- 身体中间内容 -->
      <div class="list-body__center">
        <!-- 中间其他区域 -->
        <div v-if="isBodyOther" class="body-other">
          <slot name="body-other" />
        </div>
        <!-- 中间搜索区域 -->
        <div v-if="isBodySearch" class="body-search" :class="bodySearchShow ? '' : 'body-search-show'">
          <slot name="body-search" />
        </div>
        <!-- 中间其他区域 -->
        <div v-if="isBodyNeck" class="body-other">
          <slot name="body-neck" />
        </div>
        <!-- 中间表格区域 -->
        <div class="body-container">
          <slot />
          <!-- 全屏 -->
          <div v-if="fullScreenIsShow" class="body-container_icon" @click="isFullScreen = !isFullScreen">
            <el-tooltip effect="dark" :content="`${ isFullScreen ? '退出' : '' }全屏`" placement="top">
              <el-icon class="iconfont" :size="20">
                <ZoomOut v-if="isFullScreen" />
                <FullScreen v-else />
              </el-icon>
            </el-tooltip>
          </div>
        </div>
      </div>
      <!-- 身体右侧内容 -->
      <div v-if="isBodyRight" class="list-body__rt">
        <slot name="body-right" />
      </div>
    </div>
    <!-- 详情 -->
    <slot name="list-detail" />
  </div>
</template>

<script setup name="ZSList">
import useUserStore from '@/store/modules/user';
const userStore = useUserStore();
const { loading, title, isBodyOther, isBodyNeck, isBodySearch, isBodyLeft, isBodyRight, isFold, fullScreenIsShow, isHead, isFull } = defineProps({
  // 加载动画
  loading: {
    type: Boolean,
    default: false
  },
  // 界面标题
  title: {
    type: String,
    default: '全局列表组件'
  },
  // 是否加载列表 其他
  isBodyOther: {
    type: Boolean,
    default: false
  },
  // 是否加载 其他
  isBodyNeck: {
    type: Boolean,
    default: false
  },
  // 是否加载列表搜索
  isBodySearch: {
    type: Boolean,
    default: true
  },
  // 是否加载列表左侧布局
  isBodyLeft: {
    type: Boolean,
    default: false
  },
  // 是否加载列表右侧布局
  isBodyRight: {
    type: Boolean,
    default: false
  },
  // 搜索区域是否可 收起/展开
  isFold: {
    type: Boolean,
    default: true
  },
  // 是否可全屏
  fullScreenIsShow: {
    type: Boolean,
    default: true
  },
  // 是否展示标题头部
  isHead: {
    type: Boolean,
    default: true
  },
  // 是否全屏展示
  isFull: {
    type: Boolean,
    default: false
  }
});

const bodySearchShow = ref(true);
const isFullScreen = ref(false); // 是否全屏

</script>

<style lang="scss" scoped>
  .main-list {
    position: fixed;
    top: 35px;
    right: 0;
    bottom: 35px;
    left: 200px;
    z-index: 100;
    background-color: $bg-gray;
    overflow: hidden;
    transition: 0.8s;

    .list-head {
      width: 100%;
      height: 35px;
      padding: 0 8px;
      background-color: $bg-white;
      position: relative;
      display: flex;
      align-items: center;
      box-sizing: border-box;

      &__lt {
        margin-right: auto;
        display: flex;
        align-items: center;


        &::before {
          content: '';
          display: inline-block;
          width: 4px;
          height: 14px;
          margin-right: 8px;
          background-color: $--color-primary;
        }
      }

      &__rt {
        display: flex;
        align-items: center;

        &-icon {
          cursor: pointer;
          margin-left: 8px;
        }


        .detail-close {
          display: inline-flex;
          align-items: center;
          justify-content: center;
          margin-left: 16px;
          width: 25px;
          height: 25px;
          cursor: pointer;
        }
      }
    }

    .list-body {
      position: absolute;
      top: 35px;
      left: 0;
      right: 0;
      bottom: 0;
      padding: 8px;
      display: flex;
      overflow: hidden;

      &__lt {
        min-width: 200px;
        height: 100%;
        margin-right: 8px;
        background-color: $bg-white;
        position: relative;
        overflow: hidden;
      }

      &__center {
        flex: 1;
        overflow: hidden;
        display: flex;
        flex-direction: column;

        .body-other {
          padding-bottom: 8px;
        }

        .body-search {
          padding: 16px 8px 0 8px;
          margin-bottom: 8px;
          background-color: $bg-white;
          position: relative;
          overflow: hidden;
          transition: 0.6s;
        }

        .body-search-show {
          width: 100%;
          height: 0;
          padding: 0;
          margin-bottom: 0;
        }

        .body-container {
          flex: 1;
          // padding: 16px;
          background-color: $bg-white;
          position: relative;
          overflow: hidden;

          &_icon {
            position: absolute;
            bottom: 16px;
            right: 24px;
            width: 28px;
            height: 28px;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            border-radius: 4px;
            transition: 0.4s;
            z-index: 9;

            &:hover {
              background-color: $bg-gray-light;
            }

            .iconfont {
              font-size: 22px;
            }
          }
        }
      }

      &__rt {
        min-width: 200px;
        height: 100%;
        margin-left: 16px;
        background-color: $bg-white;
        position: relative;
        overflow: hidden;
      }
    }
  }

  .full-screen {
    width: auto;
    height: auto;
    border-radius: 0px;
    border-width: 0;
    left: 0 !important;
    right: 0;
    top: 0;
    bottom: 0;
  }
</style>
