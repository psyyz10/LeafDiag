function [W,a,mu,v]=GMM_EM_3D(x,num)
%%
%Estimating the parameters of 3D Gaussian mixtrure model with EM algorithm
%%
%Reference:
%Bilmes, J. (1998). A gentle tutorial of the EM algorithm and its
%application to parameter estimation for Gaussian mixture and
%hidden Markov models. Available at http://citeseer.ist.psu.edu/
%bilmes98gentle.html.
%%
%input:
%x:The sample. np-by-3 array.
%num: the numbers of Gaussian funtions.
%output:
%W: a prior probability for the sample belong to different categories.
%Remark: W is very important for classification problems, such as image
%segmentation, mixed noise removing, impulse noise removing and so on.
%a,mu,and v are the estimated mixture coefficients,
%means and covariance matrices, respectively.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
x=double(x);
%Initial parameters.
a=ones(1,num)/num;
mu=cell(1,num);
maxx=max(x(:));
minx=min(x(:));
for i=1:num
    mu{i}=[i/(1+num)*(maxx-minx)+minx i/(1+num)*...
        (maxx-minx)+minx i/(1+num)*(maxx-minx)+minx];
end
v=cell(1,num);
for i=1:num
    v{i}=[maxx 0 0;0 maxx 0;0 0 maxx];
end
%Computing the logarithm likelihood function value.
p=gauss_distribution_3D(x,mu,v,a);
p_mix=sum(p,2)+eps;
log_likelihood=sum(log(p_mix));
iter=0;
%The iteration.
while 1
    for l=1:num
        p_l=p(:,l)./p_mix; W(:,l)=p_l;
        
        sum_p_l=sum(p_l);
        a(l)=sum_p_l/length(x);     %update mixture coefficients a
        mu{l}(1)=sum(x(:,1).*p_l)/sum_p_l;   %update means mu
        mu{l}(2)=sum(x(:,2).*p_l)/sum_p_l;
        mu{l}(3)=sum(x(:,3).*p_l)/sum_p_l;
        %update covariance matrices v.
        Y=x-repmat(mu{l},size(x,1),1);
        Z=Y.*(repmat(p_l,1,3));
        v{l}=Y'*Z/sum_p_l;
        if det(v{l})<eps
            %            error(['det(v{' num2str(l) '})=0']);
            disp('segmentation prameter needs to be adjusted')
            return;
        end
    end
    
    a=a/sum(a);
    p=gauss_distribution_3D(x,mu,v,a);
    p_mix=sum(p,2)+eps;
    new_log_likelihood=sum(log(p_mix+eps));
    err=new_log_likelihood-log_likelihood;
    log_likelihood=new_log_likelihood;
    %fprintf('iteration=%d  LF=%f\n',iter,new_log_likelihood);
    
%     [maxp ind]=max(W,[],2);
%     s = ones(length(x),1) * 3;
%     h = figure;
%     scatter3(x(:,1),x(:,2),x(:,3),s,ind);
%     
%     switch mod(iter,10)
%         case 1
%             title(['Gprah of pixel classifiction in the ',int2str(iter), 'st iteration']);
%         case 2
%             title(['Gprah of pixel classifiction in the ' , int2str(iter) , 'nd iteration']);
%         case 3
%             title(['Gprah of pixel classifiction in the ' , int2str(iter) , 'rd iteration']);
%         otherwise
%             title(['Gprah of pixel classifiction in the ' , int2str(iter) , 'th iteration']);
%     end
%     
%     xlabel('red value');
%     ylabel('green value');
%     zlabel('blue value');
    %legend('class 1','class 2','class3')
    
    iter=iter+1;
    if err<0.2 || iter>500
        break;
    end
    
    
end
%%
function p=gauss_distribution_3D(x,mu,var,cof)
%Weighted 3D Gaussian
%input:
%x:sample.np-by-3 array.
%mu:means.1-by-num cell.
%var:covariance matrices.1-by-num cell.
%cof: mixture coefficients. 1-by-num array.
%output:
%p:np-by-num array.
m=length(x);
n=length(mu);
p=repmat(0,m,n);
for i=1:n
    b=pinv(var{i});
    a=det(var{i});
    y=cat(2,x(:,1)-mu{i}(1),x(:,2)-mu{i}(2),x(:,3)-mu{i}(3));
    c=sum((y*b).*y,2);
    p(:,i)=cof(i)*exp(-c/2)/(sqrt((2*pi)^3)*sqrt(a)+eps);
end