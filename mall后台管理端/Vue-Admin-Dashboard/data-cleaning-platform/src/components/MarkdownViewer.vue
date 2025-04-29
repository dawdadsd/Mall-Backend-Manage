<template>
  <div class="markdown-container" ref="container">
    <div v-html="renderedContent"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, watch, onMounted, nextTick } from 'vue';
import { marked } from 'marked';
import mermaid from 'mermaid';

export default defineComponent({
  name: 'MarkdownViewer',
  props: {
    content: {
      type: String,
      required: true
    },
    theme: {
      type: String,
      default: 'default' // default, dark, forest, neutral
    }
  },
  setup(props) {
    const container = ref<HTMLElement | null>(null);
    
    // 初始化mermaid
    onMounted(() => {
      mermaid.initialize({
        startOnLoad: true,
        theme: props.theme,
        securityLevel: 'loose',
        flowchart: { htmlLabels: true }
      });
    });
    
    // 渲染Markdown内容
    const renderedContent = computed(() => {
      if (!props.content) return '';
      return marked(props.content);
    });
    
    // 监视内容变化，重新渲染Mermaid图表
    watch(() => props.content, () => {
      nextTick(() => {
        if (container.value) {
          try {
            // 重新初始化mermaid图表
            const diagrams = container.value.querySelectorAll('.mermaid');
            mermaid.init(undefined, diagrams);
          } catch (error) {
            console.error('Mermaid渲染错误:', error);
          }
        }
      });
    }, { immediate: true });
    
    return {
      container,
      renderedContent
    };
  }
});
</script>

<style scoped>
.markdown-container {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  line-height: 1.6;
  color: #333;
  max-width: 100%;
  overflow-x: auto;
}

/* Markdown样式 */
.markdown-container :deep(h1) {
  font-size: 2em;
  margin-top: 0.67em;
  margin-bottom: 0.67em;
  border-bottom: 1px solid #eaecef;
  padding-bottom: 0.3em;
}

.markdown-container :deep(h2) {
  font-size: 1.5em;
  margin-top: 0.83em;
  margin-bottom: 0.83em;
  border-bottom: 1px solid #eaecef;
  padding-bottom: 0.3em;
}

.markdown-container :deep(h3) {
  font-size: 1.17em;
  margin-top: 1em;
  margin-bottom: 1em;
}

.markdown-container :deep(h4) {
  font-size: 1em;
  margin-top: 1.33em;
  margin-bottom: 1.33em;
}

.markdown-container :deep(p) {
  margin-top: 1em;
  margin-bottom: 1em;
}

.markdown-container :deep(code) {
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  background-color: rgba(27, 31, 35, 0.05);
  padding: 0.2em 0.4em;
  border-radius: 3px;
  font-size: 85%;
}

.markdown-container :deep(pre) {
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  background-color: #f6f8fa;
  border-radius: 3px;
  padding: 16px;
  overflow: auto;
  font-size: 85%;
  line-height: 1.45;
}

.markdown-container :deep(pre code) {
  background-color: transparent;
  padding: 0;
  border-radius: 0;
}

.markdown-container :deep(blockquote) {
  padding: 0 1em;
  color: #6a737d;
  border-left: 0.25em solid #dfe2e5;
  margin: 0;
}

.markdown-container :deep(table) {
  border-collapse: collapse;
  width: 100%;
  overflow: auto;
  display: block;
  margin-bottom: 16px;
}

.markdown-container :deep(table th),
.markdown-container :deep(table td) {
  padding: 6px 13px;
  border: 1px solid #dfe2e5;
}

.markdown-container :deep(table tr) {
  background-color: #fff;
  border-top: 1px solid #c6cbd1;
}

.markdown-container :deep(table tr:nth-child(2n)) {
  background-color: #f6f8fa;
}

.markdown-container :deep(ul),
.markdown-container :deep(ol) {
  padding-left: 2em;
  margin-top: 0;
  margin-bottom: 16px;
}

.markdown-container :deep(img) {
  max-width: 100%;
  box-sizing: content-box;
}

.markdown-container :deep(.mermaid) {
  text-align: center;
  margin: 20px 0;
}

/* 代码高亮样式 */
.markdown-container :deep(.hljs-keyword),
.markdown-container :deep(.hljs-selector-tag),
.markdown-container :deep(.hljs-subst) {
  color: #d73a49;
}

.markdown-container :deep(.hljs-string),
.markdown-container :deep(.hljs-regexp),
.markdown-container :deep(.hljs-addition),
.markdown-container :deep(.hljs-attribute),
.markdown-container :deep(.hljs-meta-string) {
  color: #032f62;
}

.markdown-container :deep(.hljs-built_in),
.markdown-container :deep(.hljs-class .hljs-title) {
  color: #e36209;
}

.markdown-container :deep(.hljs-comment),
.markdown-container :deep(.hljs-quote) {
  color: #6a737d;
}

.markdown-container :deep(.hljs-number),
.markdown-container :deep(.hljs-literal) {
  color: #005cc5;
}
</style> 