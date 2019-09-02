package com.zgpeace.demodbdruid;

import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class ConnectionLogFilter extends FilterEventAdapter {
  @Override
  public void connection_connectBefore(FilterChain chain, Properties info) {
    super.connection_connectBefore(chain, info);
    log.info("BEFORE CONNECTION!");
  }

  @Override
  public void connection_connectAfter(ConnectionProxy connection) {
    super.connection_connectAfter(connection);
    log.info("AFTER CONNECTION!");
  }
}
