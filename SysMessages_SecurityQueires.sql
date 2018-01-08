INSERT INTO [Naid].[SecurityCode] ([Name], [Description], [Category], [LegacyCode], [UpdatedBy]) VALUES (N'SysMessage', N'Allows users to manage System Messages', N'System Messages', N'N/A', 11)
id = 1001
INSERT INTO [Naid].[SecurityPermissions] ([SecurityCode], [JobFunctionCode], [UpdatedBy]) VALUES (1001, 4, 11); id=1154
INSERT INTO [Naid].[SecurityPermissions] ([SecurityCode], [JobFunctionCode], [UpdatedBy]) VALUES (1001, 5, 11); id=1155
INSERT INTO [Naid].[SecurityPermissions] ([SecurityCode], [JobFunctionCode], [UpdatedBy]) VALUES (1001, 11, 11); id=1156
INSERT INTO [Naid].[SecurityPermissions] ([SecurityCode], [JobFunctionCode], [UpdatedBy]) VALUES (1001, 19, 11); id=1157
CREATE TABLE [Naid].[MessagesHistory] (
[Id] INT NOT NULL,
[Message]	VARCHAR(MAX) NOT NULL,
[UserName] VARCHAR (6) NOT NULL,
[StartDate] datetime2(7) NOT NULL,
[EndDate] datetime2(7) NOT NULL,
[Title] NVARCHAR(50) NOT NULL,
[IsWarning] BIT NOT NULL,
[UpdatedBy] INT NULL,
[ValidFrom] DATETIME2 (7) NOT NULL,
[ValidTo] DATETIME2 (7) NOT NULL
);
CREATE TABLE [Naid].[Messages] (
    [Id]          INT                                         IDENTITY (1, 1) NOT NULL,
    [Message]	VARCHAR(MAX)                                NOT NULL,
    [UserName] VARCHAR (6)                                NOT NULL,
    [StartDate] datetime2(7)                                NOT NULL,
    [EndDate] datetime2(7)                                NOT NULL,
[Title] NVARCHAR(50) NOT NULL,
[IsWarning] BIT NOT NULL,
    [UpdatedBy]   INT                                         DEFAULT ((11)) NULL,
    [ValidFrom]   DATETIME2 (7) GENERATED ALWAYS AS ROW START DEFAULT (sysutcdatetime()) NOT NULL,
    [ValidTo]     DATETIME2 (7) GENERATED ALWAYS AS ROW END   DEFAULT (CONVERT([datetime2],'9999-12-31 23:59:59.9999999')) NOT NULL,
    PRIMARY KEY CLUSTERED ([Id] ASC),
    CONSTRAINT [FK_Messages.UpdatedById_Account.Id] FOREIGN KEY ([UpdatedBy]) REFERENCES [Naid].[Account] ([Id]),
    PERIOD FOR SYSTEM_TIME ([ValidFrom], [ValidTo])
)
WITH (SYSTEM_VERSIONING = ON (HISTORY_TABLE=[Naid].[MessagesHistory], DATA_CONSISTENCY_CHECK=ON));




SET IDENTITY_INSERT [NAID].[SecurityCode] ON
INSERT INTO [Naid].[SecurityCode] ([Id],[Name], [Description], [Category], [LegacyCode], [UpdatedBy]) VALUES (143,N'SysMessage', N'Allows users to manage System Messages', N'System Messages', N'N/A', 11);
SET IDENTITY_INSERT [NAID].[SecurityCode] OFF


INSERT INTO [Naid].[SecurityPermissions] ([SecurityCode], [JobFunctionCode], [UpdatedBy]) VALUES (143, 4, 11); 
INSERT INTO [Naid].[SecurityPermissions] ([SecurityCode], [JobFunctionCode], [UpdatedBy]) VALUES (143, 5, 11); 
INSERT INTO [Naid].[SecurityPermissions] ([SecurityCode], [JobFunctionCode], [UpdatedBy]) VALUES (143, 11, 11);
INSERT INTO [Naid].[SecurityPermissions] ([SecurityCode], [JobFunctionCode], [UpdatedBy]) VALUES (143, 19, 11);
