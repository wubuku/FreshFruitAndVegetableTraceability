# push-to-github.ps1

# 设置要推送的分支
$branch = "main"  # 根据需要修改为你的分支名称

# 无限循环，直到成功推送
while ($true) {
    # 执行 git push 命令
    git push origin $branch

    # 检查命令的退出代码
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Push successful!" -ForegroundColor Green
        break  # 退出循环
    }
    else {
        Write-Host "Push failed. Retrying..." -ForegroundColor Red
        Start-Sleep -Seconds 2  # 等待 5 秒后重试
    }
}