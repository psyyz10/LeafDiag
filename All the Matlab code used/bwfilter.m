function img_rgb = bwfilter(img_rgb,bw)
[nrow, ncol]=size(bw);
for i = 1:nrow
	for j = 1:ncol
		if bw(i,j) == 0
			img_rgb(i,j,1) = 0;
			img_rgb(i,j,2) = 0;
			img_rgb(i,j,3) = 0;
		end;
	end;
end;