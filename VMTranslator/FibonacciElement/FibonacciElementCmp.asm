@256
D=A;
@0
M=D;
@Sys.init$ret.-1
D=A;
@SP
M=M+1;
A=M-1;
M=D;
@LCL
D=M
@SP
M=M+1;
A=M-1;
M=D;
@ARG
D=M
@SP
M=M+1;
A=M-1;
M=D;
@THIS
D=M
@SP
M=M+1;
A=M-1;
M=D;
@THAT
D=M
@SP
M=M+1;
A=M-1;
M=D;
@SP
D=M;
@5
D=D-A;
@ARG
M=D;
@SP
D=M;
@LCL
M=D;
@Sys.init
0;JMP
(Sys.init$ret.-1)
(Main.fibonacci)
@0
D=A
@ARG
D=D+M;
@SP
D=D+M
A=D-M;
D=D-A;
D=D+M;
A=D-M;
M=D-A;
@SP
M=M+1;
@2
D=A;
@SP
M=M+1;
A=M-1;
M=D;
@SP
AM=M-1;
D=M;
A=A-1;
D=M-D;
@IF_TRUE3
D;JLT
@SP
A=M-1;
M=0;
@IF_END3
0;JMP
(IF_TRUE3)
@SP
A=M-1;
M=-1;
(IF_END3)
@SP
AM=M-1;
D=M;
@Main.vm$IF_TRUE
D;JNE
@Main.vm$IF_FALSE
0;JMP
(Main.vm$IF_TRUE)
@0
D=A
@ARG
D=D+M;
@SP
D=D+M
A=D-M;
D=D-A;
D=D+M;
A=D-M;
M=D-A;
@SP
M=M+1;
@LCL
D=M
@R13
M=D;
@5
D=A;
@R13
A=M-D;
D=M;
@R14
M=D;
@SP
AM=M-1;
D=M;
@ARG
A=M
M=D;
D=A+1;
@SP
M=D;
@4
D=A;
@R13
A=M-D;
D=M;
@LCL
M=D;
@3
D=A;
@R13
A=M-D;
D=M;
@ARG
M=D;
@2
D=A;
@R13
A=M-D;
D=M;
@THIS
M=D;
@R13
A=M-1;
D=M;
@THAT
M=D;
@R14
A=M;
0;JMP
(Main.vm$IF_FALSE)
@0
D=A
@ARG
D=D+M;
@SP
D=D+M
A=D-M;
D=D-A;
D=D+M;
A=D-M;
M=D-A;
@SP
M=M+1;
@2
D=A;
@SP
M=M+1;
A=M-1;
M=D;
@SP
AM=M-1;
D=M;
A=A-1;
M=M-D;
@Main.fibonacci$ret.13
D=A;
@SP
M=M+1;
A=M-1;
M=D;
@LCL
D=M
@SP
M=M+1;
A=M-1;
M=D;
@ARG
D=M
@SP
M=M+1;
A=M-1;
M=D;
@THIS
D=M
@SP
M=M+1;
A=M-1;
M=D;
@THAT
D=M
@SP
M=M+1;
A=M-1;
M=D;
@SP
D=M;
@6
D=D-A;
@ARG
M=D;
@SP
D=M;
@LCL
M=D;
@Main.fibonacci
0;JMP
(Main.fibonacci$ret.13)
@0
D=A
@ARG
D=D+M;
@SP
D=D+M
A=D-M;
D=D-A;
D=D+M;
A=D-M;
M=D-A;
@SP
M=M+1;
@1
D=A;
@SP
M=M+1;
A=M-1;
M=D;
@SP
AM=M-1;
D=M;
A=A-1;
M=M-D;
@Main.fibonacci$ret.17
D=A;
@SP
M=M+1;
A=M-1;
M=D;
@LCL
D=M
@SP
M=M+1;
A=M-1;
M=D;
@ARG
D=M
@SP
M=M+1;
A=M-1;
M=D;
@THIS
D=M
@SP
M=M+1;
A=M-1;
M=D;
@THAT
D=M
@SP
M=M+1;
A=M-1;
M=D;
@SP
D=M;
@6
D=D-A;
@ARG
M=D;
@SP
D=M;
@LCL
M=D;
@Main.fibonacci
0;JMP
(Main.fibonacci$ret.17)
@SP
AM=M-1;
D=M;
A=A-1;
M=M+D;
@LCL
D=M
@R13
M=D;
@5
D=A;
@R13
A=M-D;
D=M;
@R14
M=D;
@SP
AM=M-1;
D=M;
@ARG
A=M
M=D;
D=A+1;
@SP
M=D;
@4
D=A;
@R13
A=M-D;
D=M;
@LCL
M=D;
@3
D=A;
@R13
A=M-D;
D=M;
@ARG
M=D;
@2
D=A;
@R13
A=M-D;
D=M;
@THIS
M=D;
@R13
A=M-1;
D=M;
@THAT
M=D;
@R14
A=M;
0;JMP
(Sys.init)
@4
D=A;
@SP
M=M+1;
A=M-1;
M=D;
@Main.fibonacci$ret.2
D=A;
@SP
M=M+1;
A=M-1;
M=D;
@LCL
D=M
@SP
M=M+1;
A=M-1;
M=D;
@ARG
D=M
@SP
M=M+1;
A=M-1;
M=D;
@THIS
D=M
@SP
M=M+1;
A=M-1;
M=D;
@THAT
D=M
@SP
M=M+1;
A=M-1;
M=D;
@SP
D=M;
@6
D=D-A;
@ARG
M=D;
@SP
D=M;
@LCL
M=D;
@Main.fibonacci
0;JMP
(Main.fibonacci$ret.2)
(Sys.vm$WHILE)
@Sys.vm$WHILE
0;JMP

