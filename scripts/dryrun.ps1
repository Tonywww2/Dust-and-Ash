#Requires -Version 5.1
<# .SYNOPSIS
Build all versions and exercise CurseForge publishing without uploading.
No API token is needed for this local dry run.
#>
[CmdletBinding()]
param([string]$JavaHome, [switch]$Offline)
. (Join-Path $PSScriptRoot 'publish-common.ps1')
$context = Get-PublishContext
Assert-PublishConfiguration $context -DryRun
$selectedJava = Select-PublishJava $context $JavaHome
$previousJava = $env:JAVA_HOME
$gradleOptions = @('--console=plain')
if ($Offline) { $gradleOptions += '--offline' }
Push-Location $context.Repo
try {
    $env:JAVA_HOME = $selectedJava
    Write-Host "== $($context.Name) $($context.Version): dry run, no uploads ==" -ForegroundColor Cyan
    Write-Host "JDK: $selectedJava"
    Invoke-PublishGradle $context (@('buildAllVersions') + $gradleOptions)
    Show-PublishFiles $context
    Invoke-PublishGradle $context (@('publishAllVersions','-Ppublish.dryRun=true') + $gradleOptions)
    Write-Host 'Dry run passed. Nothing was uploaded.' -ForegroundColor Green
} finally { $env:JAVA_HOME = $previousJava; Pop-Location }
