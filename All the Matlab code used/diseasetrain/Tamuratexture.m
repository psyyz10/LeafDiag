function Texture = Tamuratexture(rgb)
%calculate Tamura texture feature
clc;
% t0=cputime;
I = rgb2gray(rgb);
[Nx,Ny] = size(I);
Ng=256;
G=double(I);
%calculate roughness (coarseness)
Sbest=zeros(Nx,Ny);
E0h=zeros(Nx,Ny);
E0v=zeros(Nx,Ny);
E1h=zeros(Nx,Ny);
E1v=zeros(Nx,Ny);
E2h=zeros(Nx,Ny);
E2v=zeros(Nx,Ny);
E3h=zeros(Nx,Ny);
E3v=zeros(Nx,Ny);
E4h=zeros(Nx,Ny);
E4v=zeros(Nx,Ny);
E5h=zeros(Nx,Ny);
E5v=zeros(Nx,Ny);
flag=0;

for i=1:Nx
    for j=2:Ny
        E0h(i,j)=G(i,j)-G(i,j-1);
    end
end
E0h=E0h/2;

for i=1:Nx-1
    for j=1:Ny
        E0v(i,j)=G(i,j)-G(i+1,j);
    end
end
E0v=E0v/2;

%picture size must be 4*4 to calculate E1h, E1v
if (Nx<4||Ny<4)
    flag=1;
end
if(flag==0)
    for i=1:Nx-1
        for j=3:Ny-1
            E1h(i,j)=sum(sum(G(i:i+1,j:j+1)))-sum(sum(G(i:i+1,j-2:j-1)));
        end
    end
    for i=2:Nx-2
        for j=2:Ny
            E1v(i,j)=sum(sum(G(i-1:i,j-1:j)))-sum(sum(G(i+1:i+2,j-1:j)));
        end
    end
    E1h=E1h/4;
    E1v=E1v/4;
end


%picture size must be 8*8 to calculate E2h, E2v
if (Nx<8||Ny<8)
    flag=1;
end
if(flag==0)
    for i=2:Nx-2
        for j=5:Ny-3
            E2h(i,j)=sum(sum(G(i-1:i+2,j:j+3)))-sum(sum(G(i-1:i+2,j-4:j-1)));
        end
    end
    for i=4:Nx-4
        for j=3:Ny-1
            E2v(i,j)=sum(sum(G(i-3:i,j-2:j+1)))-sum(sum(G(i+1:i+4,j-2:j+1)));
        end
    end
    E2h=E2h/16;
    E2v=E2v/16;
end


%picture size must be 16*16 to calculate E3h, E3v
if (Nx<16||Ny<16)
    flag=1
end
if(flag==0)
    for i=4:Nx-4
        for j=9:Ny-7
            E3h(i,j)=sum(sum(G(i-3:i+4,j:j+7)))-sum(sum(G(i-3:i+4,j-8:j-1)));
        end
    end
    for i=8:Nx-8
        for j=5:Ny-3
            E3v(i,j)=sum(sum(G(i-7:i,j-4:j+3)))-sum(sum(G(i+1:i+8,j-4:j+3)));
        end
    end
    E3h=E3h/64;
    E3v=E3v/64;
end


%picture size must be 32*32 to calculate E4h, E4v
if (Nx<32||Ny<32)
    flag=1;
end
if(flag==0)
    for i=8:Nx-8
        for j=17:Ny-15
            E4h(i,j)=sum(sum(G(i-7:i+8,j:j+15)))-sum(sum(G(i-7:i+8,j-16:j-1)));
        end
    end
    for i=16:Nx-16
        for j=9:Ny-7
            E4v(i,j)=sum(sum(G(i-15:i,j-8:j+7)))-sum(sum(G(i+1:i+16,j-8:j+7)));
        end
    end
    E4h=E4h/256;
    E4v=E4v/256;
end


%picture size must be 64*64 to calculate E5h, E5v
if (Nx<64||Ny<64)
    flag=1;
end
if(flag==0)
    for i=16:Nx-16
        for j=33:Ny-31
            E5h(i,j)=sum(sum(G(i-15:i+16,j:j+31)))-sum(sum(G(i-15:i+16,j-32:j-31)));
        end
    end
    for i=32:Nx-32
        for j=17:Ny-15
            E5v(i,j)=sum(sum(G(i-31:i,j-16:j+15)))-sum(sum(G(i+1:i+32,j-16:j+15)));
        end
    end
    E5h=E5h/1024;
    E5v=E5v/1024;
end

for i=1:Nx
    for j=1:Ny
        [maxv,index]=max([E0h(i,j),E0v(i,j),E1h(i,j),E1v(i,j),E2h(i,j),E2v(i,j),E3h(i,j),E3v(i,j),E4h(i,j),E4v(i,j),E5h(i,j),E5v(i,j)]);
        k=floor((index+1)/2);
        Sbest(i,j)=2.^k;
    end
end
Fcoarseness=sum(sum(Sbest))/(Nx*Ny);
%calculate contrast ratio
[counts,graylevels]=imhist(I);
PI=counts/(Nx*Ny);
averagevalue=sum(graylevels.*PI);
u4=sum((graylevels-repmat(averagevalue,[256,1])).^4.*PI);
standarddeviation=sum((graylevels-repmat(averagevalue,[256,1])).^2.*PI);
alpha4=u4/standarddeviation^2;
Fcontrast=sqrt(standarddeviation)/alpha4.^(1/4);
%calculate direction ratio
PrewittH=[-1 0 1;-1 0 1;-1 0 1];
PrewittV=[1 1 1;0 0 0;-1 -1 -1];
%calculate horizontal gradient
deltaH=zeros(Nx,Ny);
for i=2:Nx-1
    for j=2:Ny-1
        deltaH(i,j)=sum(sum(G(i-1:i+1,j-1:j+1).*PrewittH));
    end
end
for j=2:Ny-1
    deltaH(1,j)=G(1,j+1)-G(1,j);
    deltaH(Nx,j)=G(Nx,j+1)-G(Nx,j);   
end
for i=1:Nx
    deltaH(i,1)=G(i,2)-G(i,1);
    deltaH(i,Ny)=G(i,Ny)-G(i,Ny-1);   
end
%calculate vertical gradient
deltaV=zeros(Nx,Ny);
for i=2:Nx-1
    for j=2:Ny-1
        deltaV(i,j)=sum(sum(G(i-1:i+1,j-1:j+1).*PrewittV));
    end
end
for j=1:Ny
    deltaV(1,j)=G(2,j)-G(1,j);
    deltaV(Nx,j)=G(Nx,j)-G(Nx-1,j);   
end
for i=2:Nx-1
    deltaV(i,1)=G(i+1,1)-G(i,1);
    deltaV(i,Ny)=G(i+1,Ny)-G(i,Ny);   
end
%the length of gradient vector
deltaG=(abs(deltaH)+abs(deltaV))/2;
%the direction of gradient direction
theta=zeros(Nx,Ny);
for i=1:Nx
    for j=1:Ny
        if (deltaH(i,j)==0)&&(deltaV(i,j)==0)
        elseif deltaH(i,j)==0
            theta(i,j)=pi;            
        else           
            theta(i,j)=atan(deltaV(i,j)/deltaH(i,j))+pi/2;
        end
    end
end
theta1=reshape(theta,1,[]);
phai=0:0.0001:pi;
HD1=hist(theta1,phai);
HD1=HD1/(Nx*Ny);
HD2=zeros(size(HD1));
%define a threshold 
THRESHOLD=0.01;
for m=1:length(HD2)
    if HD1(m)>=THRESHOLD
        HD2(m)=HD1(m);
    end
end
[c,index]=max(HD2);
phaiP=index*0.0001;
Fdirection=0;
for m=1:length(HD2)
    if HD2(m)~=0
        Fdirection=Fdirection+(phai(m)-phaiP)^2*HD2(m);
    end
end 
Texture=[Fcoarseness Fcontrast Fdirection];
% disp('Coarseness:');display(Fcoarseness)
% disp('Constrast:');display(Fcontrast)
% disp('Direction:');display(Fdirection)
% deltaT=cputime-t0;
% display(deltaT);
Texture=Texture/sum(Texture);
        
        

