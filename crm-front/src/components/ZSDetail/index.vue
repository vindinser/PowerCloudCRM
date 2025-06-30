<!-- 全局详情界面布局组件 -->
<template>
  <div class="main-detail">
    <div v-if="isMask" class="main-detail-mask faster" :class="{ animated: isHead, fadeIn: status, fadeOut: !status }" @click="handleClose" />
    <div v-loading="loading" class="main-detail-container animated faster" :class="status ? 'fadeInRight' : 'fadeOutRight'" :style="{ width: getUnitValue(width) }">
      <!-- 详情头部 -->
      <div v-if="isHead" class="detail-head">
        <div class="detail-head__inner">
          <!-- 详情头部的左侧内容 · 标题展示 · 可使用插槽自定义内容 -->
          <div class="detail-head__inner-lt">
            <slot name="head-left">
              <div class="custom-title">{{ title }}</div>
            </slot>
          </div>
          <!-- 详情头部的右侧内容 · 插槽自定义内容 -->
          <div class="detail-head__inner-rt">
            <slot name="head-right" />
            <!-- 右侧关闭按钮 -->
            <el-button v-if="showClose" type="danger" :icon="CloseBold" circle class="detail-close" @click="handleClose" />
          </div>
        </div>
        <div class="detail-head__expand">
          <slot name="head-expand" />
        </div>
      </div>
      <!-- 详情身体 -->
      <div class="detail-body">
        <!-- 身体左侧内容 · 树状控件 -->
        <div v-if="isBodyLeft" class="detail-body__lt">
          <slot name="body-left" />
        </div>
        <!-- 身体中间内容 -->
        <div class="detail-body__center">
          <!-- 中间搜索区域 -->
          <div v-if="isBodySearch" class="body-search">
            <slot name="body-search" />
          </div>
          <!-- 中间表格区域 -->
          <div v-infinite-scroll="loadmore" class="body-container" infinite-scroll-distance='1' @scroll.passive="getScroll($event)">
            <!-- 回到顶部 -->
            <el-backtop v-if="isScollToTop" target=".detail-body__center .body-container" :right="56" :bottom="56" :visibility-height="100" />
            <slot />
          </div>
        </div>
        <!-- 身体右侧内容 · 拍卖会口袋 -->
        <div v-if="isBodyRight" class="detail-body__rt">
          <slot name="body-right" />
        </div>
      </div>
      <!-- 详情底部 -->
      <div v-if="isFoot" class="detail-foot">
        <slot name="foot" />
      </div>
    </div>
    <!-- 其它东西存放 -->
    <slot name="detail-other" />
  </div>
</template>

<script setup name="ZSDetail">
  /**
   * MainDetail 全局  详情界面布局组件
   * @description 弹出层容器，用于展示弹窗、信息提示等内容，支持上、下、左、右和中部弹出。组件只提供容器，内部内容由用户自定义
   * @property {String} mode 弹出方向（默认left）
   * @property {Boolean} mask 是否显示遮罩（默认true）
   * @property {Stringr | Number} length mode=left | 见官网说明（默认auto）
   * @property {Boolean} zoom 是否开启缩放动画，只在mode为center时有效（默认true）
   * @property {Boolean} safe-area-inset-bottom 是否开启底部安全区适配（默认false）
   * @property {Boolean} mask-close-able 点击遮罩是否可以关闭弹出层（默认true）
   * @property {Object} custom-style 用户自定义样式
   * @property {Stringr | Number} negative-top 中部弹出时，往上偏移的值
   * @property {Numberr | String} border-radius 弹窗圆角值（默认0）
   * @property {Numberr | String} z-index 弹出内容的z-index值（默认1075）
   * @property {Boolean} closeable 是否显示关闭图标（默认false）
   * @property {String} close-icon 关闭图标的名称，只能uView的内置图标
   * @property {String} close-icon-pos 自定义关闭图标位置（默认top-right）
   * @property {String} close-icon-color 关闭图标的颜色（默认#909399）
   * @property {Number | String} close-icon-size 关闭图标的大小，单位rpx（默认30）
   * @event {Function} open 弹出层打开
   * @event {Function} close 弹出层收起
   * @example <u-popup v-model="show"><view>出淤泥而不染，濯清涟而不妖</view></u-popup>
   */
  import { CloseBold } from '@element-plus/icons-vue';

  // Props 定义
  const props = defineProps({
    // 详情的宽度
    width: {
      type: [Number, String],
      default: 70
    },
    // 加载动画
    loading: {
      type: Boolean,
      default: false
    },
    // 界面标题
    title: {
      type: String,
      default: '全局详情组件'
    },
    // 是否加载遮罩层
    isMask: {
      type: Boolean,
      default: true
    },
    isHead: {
      type: Boolean,
      default: true
    },
    isFoot: {
      type: Boolean,
      default: false
    },
    // 是否加载详情左侧布局
    isBodyLeft: {
      type: Boolean,
      default: false
    },
    // 是否加载详情右侧布局
    isBodyRight: {
      type: Boolean,
      default: false
    },
    // 关闭前的回调，会暂停详情的关闭
    beforeClose: {
      type: Function,
      default: null
    },
    // 是否监听滚动条
    isScroll: {
      type: Boolean,
      default: false
    },
    // 是否显示关闭图标
    showClose: {
      type: Boolean,
      default: true
    },
    // 是否展示回到顶部
    isScollToTop: {
      type: Boolean,
      default: false
    },
    isBodySearch: {
      type: Boolean,
      default: false
    }
  });

  // Emits 定义
  const emit = defineEmits(['close', 'closed', 'showHeadSteps', 'wetherActive', 'loadmore']);

  // 响应式数据
  const status = ref(true);

  // 方法定义
  // 判断传入的值，是否带有单位，如果没有，就默认用rpx单位
  const getUnitValue = (val) => {
    if (/(%|px|rpx|auto)$/.test(val)) {return val}
    return val + '%';
  };

  // 关闭详情
  const close = (boo = false) => {
    // 关闭详情
    status.value = false;
    emit('close');
    setTimeout(() => emit('closed', boo), 300);
  };

  const handleClose = () => {
    if (typeof props.beforeClose === 'function') {
      props.beforeClose(close);
    } else {
      close();
    }
  };

  // 监听滚动条滚动事件
  const getScroll = (event) => {
    if(props.isScroll) {
      emit('showHeadSteps', event.target.scrollTop);
      // if(event.target.scrollTop > 100) {
      //   emit('showHeadSteps', true)
      // } else{
      //   emit('showHeadSteps', false)
      // }

      // 滚动条距离底部的距离scrollBottom
      let scrollBottom = event.target.scrollHeight - event.target.scrollTop - event.target.clientHeight;

      if(event.target.scrollTop < document.getElementById('receiveInfo').offsetTop - 10) {
        emit('wetherActive', '1');
      } else if(scrollBottom < 50) {
        emit('wetherActive', '3');
      } else {
        emit('wetherActive', '2');
      }
    }
  };

  // 加载更多
  const loadmore = () => {
    emit('loadmore');
  };

  // 滚动至页面底部
  // const handleScrollBottom = () => {
  //   let scrollElem = document.getElementsByClassName('body-container')[1];

  //   scrollElem.scrollTo({ top: scrollElem.scrollHeight, behavior: 'smooth' });
  // };

</script>

<style lang="scss" scoped>
.main-detail {

  &-mask {
    width: 100%;
    height: 100%;
    background-color: rgba($color: $bg-black, $alpha: 0.5);
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 101;
  }

  &-container {
    max-width: 100%;
    // background-color: $bg-gray;
    background-color: $bg-white;
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    z-index: 102;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .detail-head {
      background-color: $bg-white;
      box-shadow: inset 0px -1px 0px $bg-gray-shallow;
      position: relative;

      &__inner {
        box-sizing: border-box;
        width: 100%;
        min-height: 48px;
        padding: 0 16px;
        display: flex;
        align-items: center;

        &-lt {
          margin-right: auto;
          display: flex;
          align-items: center;

          .custom-title{
            font-size: 16px;
            line-height: 48px;
            font-weight: 600;
            display: flex;
            align-items: center;

            &::before {
              content: '';
              display: inline-block;
              width: 4px;
              height: 12px;
              margin-right: 8px;
              background-color: $--color-primary;
            }
          }
        }

        &-rt{
          display: flex;
          align-items: center;
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

    .detail-body {
      flex: 1;
      display: flex;
      overflow: hidden;
      padding: 10px;

      &__lt {
        min-width: 200px;
        height: 100%;
        margin-right: 16px;
        background-color: $bg-white;
        position: relative;
        overflow: hidden;
      }

      &__center {
        flex: 1;
        overflow: hidden;
        display: flex;
        flex-direction: column;

        .body-search {
          padding: 16px 16px 0 16px;
          margin-bottom: 16px;
          background-color: $bg-white;
          // position: relative;
          overflow: hidden;
        }

        .body-container {
          box-sizing: border-box;
          flex: 1;
          background-color: $bg-white;
          // position: relative;
          overflow: auto;
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

    .detail-foot {
      padding: 16px;
      background-color: #fff;
      box-shadow: inset 0px 1px 0px $bg-gray-shallow;
      display: flex;
      align-items: center;
      justify-content: flex-end;
    }
  }
}
</style>
