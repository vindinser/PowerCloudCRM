/* eslint-disable no-console */
import colors from 'picocolors';

const vitePluginStart = function vitePluginVueMonitor () {

  return {
    name: 'crm-start',
    apply: 'serve',
    enforce: 'pre',
    configureServer(server) {
      const print = server.printUrls;

      server.printUrls = () => {
        const network = server.resolvedUrls?.network[0];
        const local = server.resolvedUrls?.local[0];

        if (!network && !local) {
          console.log(colors.red('获取IP地址失败,请检查vite.config.js文件中server.host配置是否正确!\n'));
        } else {
          // https://www.bootschool.net/ascii-art
          console.info(colors.green(`
                       ___________    ____ 
                ______/   \\__//   \\__/____\\ 
              _/   \\_/  :           //____\\\\ 
             /|      :  :  ..      /        \\ 
            | |     ::     ::      \\        / 
            | |     :|     ||     \\ \\______/ 
            | |     ||     ||      |\\  /  | 
             \\|     ||     ||      |   / | \\ 
              |     ||     ||      |  / /_\\ \\ 
              | ___ || ___ ||      | /  /    \\ 
               \\_-_/  \\_-_/ | ____ |/__/      \\ 
                            _\\_--_/    \\      / 
                           /____             / 
                          /     \\           / 
                          \\______\\_________/
          `));
          print();
        }
      };
    }
  };
};

const vitePluginBuild = function vitePluginVueMonitor () {

  return {
    name: 'crm-build',
    apply: 'build', // 值可以是 build 或 serve 亦可以是一个函数，指明它们仅在 build 或 serve 模式时调用
    // normal(默认值)第二批配执行的插件，会在vite的build阶段之前被执行，可以根据配置判断是否需要处理当前文件的代码。
    // pre首批被执行的插件，会在@rollup/plugin-alias插件执行之后执行。
    // post会在vite的build阶段之后被执行，进行代码构建方面的工作(minimize、代码分析...)。
    enforce: 'pre',
    closeBundle() {
      console.log(colors.green(`
                        ¶¶¶¶¶¶¶¶¶               
                      ¶¶          ¶¶            
        ¶¶¶¶¶       ¶¶              ¶¶          
       ¶     ¶    ¶¶     ¶¶    ¶¶     ¶¶        
       ¶     ¶   ¶¶      ¶¶    ¶¶       ¶¶      
       ¶    ¶  ¶¶        ¶¶    ¶¶        ¶¶     
        ¶   ¶   ¶                         ¶¶    
      ¶¶¶¶¶¶¶¶¶¶¶¶                        ¶¶    
     ¶            ¶ ¶¶             ¶¶     ¶¶    
    ¶¶            ¶  ¶¶            ¶¶     ¶¶    
   ¶¶   ¶¶¶¶¶¶¶¶¶¶¶    ¶¶        ¶¶       ¶¶    
   ¶               ¶     ¶¶¶¶¶¶¶         ¶¶     
   ¶¶              ¶                    ¶¶      
    ¶   ¶¶¶¶¶¶¶¶¶¶¶¶                   ¶¶       
    ¶¶           ¶  ¶¶                ¶¶        
     ¶¶¶¶¶¶¶¶¶¶¶¶     ¶¶            ¶¶          
                         ¶¶¶¶¶¶¶¶¶¶¶
        打包成功了奥，去上线吧
      `));
    }
  };
};

export {
  vitePluginStart,
  vitePluginBuild
};