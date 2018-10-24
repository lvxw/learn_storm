#!/bin/bash
###############################################################################
#Script:        WordCountKafkaTopology_Scala.sh
#Author:        吕学文<2622478542@qq.com>
#Date:          2018-10-08
#Description:
#Usage:
#Jira:
###############################################################################

#设置脚本运行环境和全局变量
function set_env(){
    cd `cd $(dirname $0)/../.. && pwd`
    source bin/init_context_env.sh day $1
}

#设置日、周、月的数据输入、输出路径
function init(){
    topic=test-flink
    main_class='com.test.java.business.template.WordCountKafkaTopology'
}

function execute_mr(){
    $STORM_INSTALL/bin/storm \
        jar jar/LearnStorm.jar com.test.java.business.template.WordCountKafkaTopology \
        "{\"topic\":\"${topic}\", \
          \"main_class\":\"${main_class}\", \
          \"run_pattern\":\"${RUN_PATTERN}\" \
        }"
}

set_env $1
init
execute_mr