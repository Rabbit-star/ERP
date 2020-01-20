package com.cxy.erp.job;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class TestMail {
	int i=1;
	private JavaMailSender javaMailSender;

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	public void sendMail() throws MessagingException{
		MimeMessage mime = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mime); 
		helper.setFrom("caixiaoyao520@163.com");//发件人
		helper.setTo("zhuyr1998@163.com");//收件人 
		helper.setSubject("测试邮件");//主题
		helper.setText("这是我爱你的第"+ i++ +"次");//内容 
		javaMailSender.send(mime);
		}
	public static void main(String[] args) throws MessagingException {
		ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext_mail.xml"); 
		TestMail testSend= (TestMail) context.getBean("testSend"); 
		testSend.sendMail();
	}

	
}
