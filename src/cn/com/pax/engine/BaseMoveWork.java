package cn.com.pax.engine;

import cn.com.pax.utils.ReadCfgUtils;

import javax.swing.*;

/**
 * Created by luohl on 2017-12-12
 */
public interface BaseMoveWork {
   int  MoveWork(String caseTxt, ReadCfgUtils readCfgUtils, JEditorPane recordTa);
}
