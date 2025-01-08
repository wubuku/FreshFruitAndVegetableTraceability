# pull-from-github.ps1

# 设置要拉取的分支
$branch = "main"  # 根据需要修改为你的分支名称

# 无限循环，直到成功拉取
while ($true) {
    # 执行 git pull 命令
    git pull origin $branch

    # 检查命令的退出代码
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Pull successful!" -ForegroundColor Green
        break  # 退出循环
    }
    else {
        Write-Host "Pull failed. Retrying..." -ForegroundColor Red
        Start-Sleep -Seconds 5  # 等待 5 秒后重试
    }
}