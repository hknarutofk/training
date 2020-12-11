

docker run --rm -it --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.10.1

docker run --rm -it --name kibana --link elasticsearch:elasticsearch -p 5601:5601 kibana:7.10.1


启用分词

搜索排序


kibana搜索界面、数据分析可视化

beat logstash 日志收集 flume ?




https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html

https://www.elastic.co/guide/en/elastic-stack-get-started/current/get-started-elastic-stack.html