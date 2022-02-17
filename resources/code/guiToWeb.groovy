
response.setCharacterEncoding("UTF-8");
Writer writer = response.getWriter();
response.setContentType("text/html");

writer.print(request.getParameter("test"));
system.webOut(Summary.summarytypeText.selectedItem);
writer.flush();