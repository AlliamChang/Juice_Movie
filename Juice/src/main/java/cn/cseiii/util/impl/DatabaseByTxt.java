package cn.cseiii.util.impl;

import cn.cseiii.enums.FigureType;
import cn.cseiii.enums.ResultMessage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 53068 on 2017/5/10 0010.
 */
public class DatabaseByTxt {

    public StringBuilder read(File file){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            sb = null;
        }
        return sb;
    }

    public List<String> readByEachLine(File file){
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                lines.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            lines = null;
        }
        return lines;
    }

    public ResultMessage write(File file,StringBuilder content, boolean append){
        ResultMessage resultMessage;
        try {
            FileWriter writer = new FileWriter(file,append);
            writer.write(content.toString());
            writer.flush();
            writer.close();
            resultMessage = ResultMessage.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            resultMessage = ResultMessage.FAILURE;
        }
        return resultMessage;
    }

    public ResultMessage writeByList(File file, List<String> lines, boolean append){
        ResultMessage resultMessage;
        try {
            FileWriter writer = new FileWriter(file,append);
            StringBuilder builder = new StringBuilder();
            lines.forEach(s -> builder.append(s+"\r\n"));
            writer.write(builder.toString());
            writer.close();
            resultMessage = ResultMessage.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            resultMessage = ResultMessage.FAILURE;
        }
        return resultMessage;
    }
}
