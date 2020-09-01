package io.qmeta.testng.testmodel;


import cn.hutool.core.util.StrUtil;

/**
 * 测试优先级定义
 */
public enum TestCasePriority {
        SmokeTesting("SmokeTesting",100),P1("P1",99),P2("P2",98),P3("P3",93),Others("Others",0);
        private int value;
        private String name;

        TestCasePriority(String name, int value) {
            this.value = value;
            this.name=name;
        }

        public static TestCasePriority getPriorityByName(String name){
            if(!StrUtil.isNotBlank(name)) return Others;
            for (TestCasePriority testCasePriority : TestCasePriority.values()) {
                if(testCasePriority.getName().equalsIgnoreCase(name)) return testCasePriority;
            }

            return Others;
        }

    /**
     * 判断当前测试用例是否符合配置的条件
     * 如果testng 配置为smoketesting，那么只跑smoketesting，如果配置为p1,那么跑p1和smoketesting
     * @param configuredPriority testng中参数配置的priority 的值
     * @param priority  测试用例中设置的Prority的值
     * @return boolean true or false
     */
       public static boolean isRunPriority(String configuredPriority, String priority){
            int val = getPriorityByName(configuredPriority).getValue();
            int p = getPriorityByName(priority).getValue();
            return p-val>=0;
        }

        public int getValue() {
            return value;
        }


        public String getName() {
            return name;
        }

    }
