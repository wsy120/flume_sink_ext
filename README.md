
mvn clean 
mvn install
将代码打成jar包后，上传到flume安装目录下的lib文件夹中，同时需要上传SQL的驱动jar包

-------------测试 Spooling Directory Source--------------------------
conf：dbsql_sink.conf
<code>
# Name the components on this agent
a1.sources = r1
a1.sinks = k1
a1.channels = c1

# Describe/configure the source
#a1.sources.r1.type = spooldir
#a1.sources.r1.spoolDir = /home/rui/log/flumespool
#a1.sources.r1.fileHeader = true

a1.sources.r1.type = exec
a1.sources.r1.command = tail -F /home/zl/xsvr/server/xgame_4/logs/act/zl_war.log
#a1.sources.tailsource-1.command = for i in /path/*.log; do cat $i; done
a1.sources.r1.channels = c1

# Describe the sink
a1.sinks.k1.type = com.flume.dome.mysink.DBsqlSink
a1.sinks.k1.hostname = jdbc:postgresql://192.168.20.243:5432
#a1.sinks.k1.port = 5432
a1.sinks.k1.databaseName = game_log
a1.sinks.k1.tableName = zl_log
a1.sinks.k1.user = game
a1.sinks.k1.password = game123
a1.sinks.k1.serverId = 4
a1.sinks.k1.channel = c1
a1.sinks.k1.josnTo = true   #是否进行json转换  k=v行式文本 转json



# Use a channel which buffers events in memory
a1.channels.c1.type = memory
a1.channels.c1.capacity = 1000
a1.channels.c1.transactionCapacity = 100
</code>


启动flume agent al
bin/flume-ng agent --conf conf --conf-file conf/dbsql_sink_4.conf --name a1 -Dflume.root.logger=DEBUG,console


bin/flume-ng agent --conf conf --conf-file conf/all_dir_flume_to_pgsql.conf --name a1 -Dflume.root.logger=DEBUG,console


../bin/flume-ng agent --conf ../conf --conf-file ../conf/all_dir_flume_to_pg.conf  --name a1 -Dflume.root.logger=DEBUG,console



nohup bin/flume-ng agent --conf conf --conf-file conf/dbsql_sink_4.conf --name a1 -Dflume.root.logger=INFO,console &

nohup ../bin/flume-ng agent --conf ../conf --conf-file ../conf/x1_dir_to_db_flume.conf  --name a1 -Dflume.root.logger=INFO,console > x1nohup.out 2>&1 &



创间表：
CREATE TABLE `log_data` (
`id`  int(11) NOT NULL AUTO_INCREMENT ,
`context`  varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL ,
`time`  bigint(20) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=latin1 COLLATE=latin1_swedish_ci
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT
;


测试:
cd /home/rui/log/flumespool
echo "hello world" > test.log

数据写入成功!

