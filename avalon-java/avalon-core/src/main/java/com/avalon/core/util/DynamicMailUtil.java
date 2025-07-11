package com.avalon.core.util;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.MessageHandler;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/6/19
 */
public class DynamicMailUtil {
    /**
     * 创建邮件发送器
     *
     * @param host     ip
     * @param port     端口
     * @param username 邮件名
     * @param password 密码
     * @param ssl      加密
     * @return JavaMailSenderImpl
     */
    public static JavaMailSenderImpl createSender(String host, int port, String username, String password, boolean ssl) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);
        sender.setDefaultEncoding("UTF-8");

        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        if (ssl) {
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.port", String.valueOf(port));
            props.put("mail.smtp.starttls.enable", "true");
        }

        return sender;
    }

    /**
     * 测试联通
     *
     * @param host     ip
     * @param port     端口
     * @param username 邮件名
     * @param password 密码
     * @param ssl      加密
     */
    public static void connect(String host, int port, String username, String password, boolean ssl) {
        JavaMailSenderImpl sender = createSender(host, port, username, password, ssl);
        try {
            sender.testConnection();
        } catch (MessagingException e) {
            throw new AvalonException(e.getMessage(), e);
        }
    }

    /**
     * 支持多个收件人、抄送、密送、附件
     */
    public static void sendMail(String host, int port, String username, String password, boolean ssl,
                                String[] to, String[] cc, String[] bcc,
                                String subject, String content,
                                List<File> attachments) throws MessagingException {

        JavaMailSenderImpl sender = createSender(host, port, username, password, ssl);

        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(username);

        if (to != null && to.length > 0) {
            helper.setTo(to);
        } else {
            throw new IllegalArgumentException("Recipient (to) required!");
        }
        if (cc != null && cc.length > 0) {
            helper.setCc(cc);
        }
        if (bcc != null && bcc.length > 0) {
            helper.setBcc(bcc);
        }

        helper.setSubject(subject);
        helper.setText(content, true);

        // 添加附件
        if (attachments != null) {
            for (File file : attachments) {
                if (file != null && file.exists()) {
                    helper.addAttachment(file.getName(), file);
                }
            }
        }


        sender.send(mimeMessage);
    }

    /**
     * 单人，无附件
     */
    public static void sendMail(String host, int port, String username, String password, boolean ssl,
                                String to,
                                String subject, String content) throws MessagingException {
        sendMail(host, port, username, password, ssl,
                new String[]{to}, null, null, subject, content, null);
    }

    /**
     * 获取收件箱邮件总数
     *
     * @param host       ip
     * @param port       端口
     * @param username   邮件名
     * @param password   密码
     * @param ssl        加密
     * @param folderName INBOX（收件箱）Sent（已发送）Drafts（草稿）Junk/Spam（垃圾邮件）Trash/Deleted（已删除）Archive（归档）
     * @return 总数
     */
    public static int getMailCount(String host, int port, String username, String password, boolean ssl, String folderName) {
        Properties props = new Properties();
        props.put("mail.imap.host", host);
        props.put("mail.imap.port", String.valueOf(port));
        props.put("mail.imap.ssl.enable", String.valueOf(ssl));
        Session session = Session.getInstance(props);

        Store store = null;
        Folder folder = null;
        try {
            store = session.getStore("imap");
            store.connect(username, password);
            folder = store.getFolder(folderName);
            folder.open(Folder.READ_ONLY);
            return folder.getMessageCount();
        } catch (Exception e) {
            throw new RuntimeException("获取邮件总数失败: " + e.getMessage(), e);
        } finally {
            try {
                if (folder != null && folder.isOpen()) folder.close(false);
            } catch (Exception ignore) {
            }
            try {
                if (store != null) store.close();
            } catch (Exception ignore) {
            }
        }
    }

    /**
     * 获取所有邮件文件夹（全名）
     *
     * @param host     邮件服务器
     * @param port     端口
     * @param username 邮箱名
     * @param password 密码
     * @param ssl      是否SSL
     * @return 文件夹全名列表
     */
    public static List<String> listAllMailFolders(String host, int port, String username, String password, boolean ssl) {
        Properties props = new Properties();
        props.put("mail.imap.host", host);
        props.put("mail.imap.port", String.valueOf(port));
        props.put("mail.imap.ssl.enable", String.valueOf(ssl));
        Session session = Session.getInstance(props);

        Store store = null;
        List<String> result = new java.util.ArrayList<>();
        try {
            store = session.getStore("imap");
            store.connect(username, password);
            Folder defaultFolder = store.getDefaultFolder();
            Folder[] folders = defaultFolder.list();
            for (Folder folder : folders) {
                result.add(folder.getFullName());
                listSubFolders(folder, result);
            }
        } catch (Exception e) {
            throw new RuntimeException("获取邮件文件夹失败: " + e.getMessage(), e);
        } finally {
            try {
                if (store != null) store.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    // 递归子文件夹
    private static void listSubFolders(Folder folder, List<String> result) throws MessagingException {
        if ((folder.getType() & Folder.HOLDS_FOLDERS) != 0) {
            Folder[] subFolders = folder.list();
            for (Folder sub : subFolders) {
                result.add(sub.getFullName());
                listSubFolders(sub, result);
            }
        }
    }

    /**
     * 分页获取指定文件夹下的邮件
     *
     * @param host       邮件服务器
     * @param port       端口
     * @param username   邮箱名
     * @param password   密码
     * @param ssl        是否SSL
     * @param folderName 文件夹名
     * @param page       第几页（从1开始）
     * @param pageSize   每页大小
     * @return Message[] 邮件数组（新邮件在前）
     */
    public static void getMessagesByPage(
            String host, int port, String username, String password, boolean ssl,
            String folderName, int page, int pageSize, MessageHandler handler) {
        Properties props = new Properties();
        props.put("mail.imap.host", host);
        props.put("mail.imap.port", String.valueOf(port));
        props.put("mail.imap.ssl.enable", String.valueOf(ssl));
        Session session = Session.getInstance(props);

        Store store = null;
        Folder folder = null;
        try {
            store = session.getStore("imap");
            store.connect(username, password);
            folder = store.getFolder(folderName);
            folder.open(Folder.READ_ONLY);

            int total = folder.getMessageCount();
            if (total == 0) {
                return;
            }

            int end = total - (page - 1) * pageSize;
            int start = Math.max(1, end - pageSize + 1);
            if (end < 1) {
                return;
            }
            Message[] messages = folder.getMessages(start, end);

            // 回调处理
            for (Message msg : messages) {
                handler.handle(msg);
            }
        } catch (Exception e) {
            throw new RuntimeException("分页回调处理邮件失败: " + e.getMessage(), e);
        } finally {
            try {
                if (folder != null && folder.isOpen()) folder.close(false);
            } catch (Exception ignore) {
            }
            try {
                if (store != null) store.close();
            } catch (Exception ignore) {
            }
        }
    }


    /**
     * 获取指定文件夹内所有大于指定UID的未同步新邮件
     *
     * @param host          邮件服务器地址
     * @param port          端口号
     * @param username      邮箱账号
     * @param password      密码
     * @param ssl           是否使用SSL
     * @param folderName    文件夹名称（如INBOX）
     * @param lastSyncedUID 上次同步到的最大UID
     * @return 新邮件Message数组
     */
    public static Message[] getNewMessagesByUID(
            String host, int port, String username, String password, boolean ssl,
            String folderName, long lastSyncedUID) {
        Properties props = new Properties();
        props.put("mail.imap.host", host);
        props.put("mail.imap.port", String.valueOf(port));
        props.put("mail.imap.ssl.enable", String.valueOf(ssl));
        Session session = Session.getInstance(props);

        IMAPStore store = null;
        IMAPFolder folder = null;
        try {
            store = (IMAPStore) session.getStore("imap");
            store.connect(username, password);
            folder = (IMAPFolder) store.getFolder(folderName);
            folder.open(Folder.READ_ONLY);

            // 获取所有大于lastSyncedUID的邮件
            Message[] messages = folder.getMessagesByUID(lastSyncedUID + 1, UIDFolder.LASTUID);
            return messages;

        } catch (Exception e) {
            throw new RuntimeException("获取未同步邮件失败: " + e.getMessage(), e);
        } finally {
            try {
                if (folder != null && folder.isOpen()) folder.close(false);
            } catch (Exception ignore) {
            }
            try {
                if (store != null) store.close();
            } catch (Exception ignore) {
            }
        }
    }
}
