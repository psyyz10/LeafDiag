rng('default')  % For reproducibility
mu1 = [1 2];
sigma1 = [3 .2; .2 1];
mu2 = [-1 -1];
sigma2 = [1.5 0; 0 2];
mu3 = [-3,3]
sigma3 = [2 0; 0 1.5];
X1 = [mvnrnd(mu1,sigma1,200);mvnrnd(mu2,sigma2,200); mvnrnd(mu3,sigma3,200)];

h=figure;
hold on;
scatter(X(:,1),X(:,2),10,'ko');


X=[X1;X2;X3]

options = statset('Display','final');
gm = gmdistribution.fit(X,3,'Options',options);

ezcontour(@(x,y)pdf(gm,[x y]),[-8 6],[-6 7]);
print(h,'-dpng','1');


hold off